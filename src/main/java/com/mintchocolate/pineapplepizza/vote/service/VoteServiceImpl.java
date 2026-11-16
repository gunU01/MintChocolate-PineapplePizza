package com.mintchocolate.pineapplepizza.vote.service;

import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import com.mintchocolate.pineapplepizza.vote.dto.VoteRequest;
import com.mintchocolate.pineapplepizza.vote.dto.VoteResponse;
import com.mintchocolate.pineapplepizza.vote.entity.Vote;
import com.mintchocolate.pineapplepizza.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final DebateRepository debateRepository;

    @Override
    @Transactional
    public VoteResponse createVote(Long debateId, String voterKey, VoteRequest request) {
        Debate debate = debateRepository.findById(debateId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (voteRepository.existsByDebateIdAndVoterKey(debateId, voterKey)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Vote vote = new Vote(debate, voterKey, request.getChoice());
        Vote savedVote = voteRepository.save(vote);
        return VoteResponse.from(savedVote);
    }

    @Override
    @Transactional(readOnly = true)
    public VoteResponse getMyVote(Long debateId, String voterKey) {
        if (!debateRepository.existsById(debateId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Vote vote = voteRepository.findByDebateIdAndVoterKey(debateId, voterKey)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return VoteResponse.from(vote);
    }

    @Override
    @Transactional
    public VoteResponse updateMyVote(Long debateId, String voterKey, VoteRequest request) {
        if (!debateRepository.existsById(debateId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Vote vote = voteRepository.findByDebateIdAndVoterKey(debateId, voterKey)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        vote.changeChoice(request.getChoice());
        return VoteResponse.from(vote);
    }
}
