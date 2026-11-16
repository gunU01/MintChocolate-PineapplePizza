package com.mintchocolate.pineapplepizza.debate.service;

import com.mintchocolate.pineapplepizza.comment.repository.CommentRepository;
import com.mintchocolate.pineapplepizza.debate.dto.DebatePageRequest;
import com.mintchocolate.pineapplepizza.debate.dto.DebateRequest;
import com.mintchocolate.pineapplepizza.debate.dto.DebateResponse;
import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import com.mintchocolate.pineapplepizza.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class DebateServiceImpl implements DebateService {

    private final DebateRepository debateRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public DebateResponse createDebate(DebateRequest request) {
        Debate debate = new Debate(
                request.getTitle(),
                request.getDescription(),
                request.getOptionA(),
                request.getOptionB()
        );
        Debate savedDebate = debateRepository.save(debate);
        return DebateResponse.from(savedDebate);
    }

    @Override
    public DebateResponse getDebateById(Long id) {
        Debate debate = debateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return DebateResponse.from(debate);
    }

    @Override
    public Page<DebateResponse> getDebates(String keyword, DebatePageRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id")
        );
        Page<Debate> debates;
        if (keyword == null || keyword.isBlank()) {
            debates = debateRepository.findAll(pageable);
        } else {
            debates = debateRepository.findByTitleContainingIgnoreCase(keyword.trim(), pageable);
        }
        return debates.map(DebateResponse::from);
    }

    @Override
    @Transactional
    public DebateResponse updateDebate(Long id, DebateRequest request) {
        Debate debate = debateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        boolean optionsChanged = !debate.getOptionA().equals(request.getOptionA())
                || !debate.getOptionB().equals(request.getOptionB());
        if (optionsChanged && voteRepository.existsByDebateId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        debate.update(
                request.getTitle(),
                request.getDescription(),
                request.getOptionA(),
                request.getOptionB()
        );
        return DebateResponse.from(debate);
    }

    @Override
    @Transactional
    public void deleteDebate(Long id) {
        Debate debate = debateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        voteRepository.deleteByDebateId(id);
        commentRepository.deleteByDebateId(id);
        debateRepository.delete(debate);
    }
}
