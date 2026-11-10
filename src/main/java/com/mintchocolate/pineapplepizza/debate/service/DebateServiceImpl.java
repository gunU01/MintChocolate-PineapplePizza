package com.mintchocolate.pineapplepizza.debate.service;

import com.mintchocolate.pineapplepizza.debate.dto.DebateRequest;
import com.mintchocolate.pineapplepizza.debate.dto.DebateResponse;
import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DebateServiceImpl implements DebateService {

    private final DebateRepository debateRepository;

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
}
