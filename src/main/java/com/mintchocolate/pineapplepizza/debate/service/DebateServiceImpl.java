package com.mintchocolate.pineapplepizza.debate.service;

import com.mintchocolate.pineapplepizza.comment.repository.CommentRepository;
import com.mintchocolate.pineapplepizza.debate.dto.DebateRequest;
import com.mintchocolate.pineapplepizza.debate.dto.DebateResponse;
import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import com.mintchocolate.pineapplepizza.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public List<DebateResponse> getDebates(String keyword) {
        List<Debate> debates;
        if (keyword == null || keyword.isBlank()) {
            debates = debateRepository.findAllByOrderByCreatedAtDescIdDesc();
        } else {
            debates = debateRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDescIdDesc(keyword.trim());
        }
        return debates.stream()
                .map(DebateResponse::from)
                .toList();
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
