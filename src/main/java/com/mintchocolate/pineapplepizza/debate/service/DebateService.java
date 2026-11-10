package com.mintchocolate.pineapplepizza.debate.service;

import com.mintchocolate.pineapplepizza.debate.dto.DebateRequest;
import com.mintchocolate.pineapplepizza.debate.dto.DebateResponse;

import java.util.List;

public interface DebateService {

    DebateResponse createDebate(DebateRequest request);

    DebateResponse getDebateById(Long id);

    List<DebateResponse> getDebates();

    DebateResponse updateDebate(Long id, DebateRequest request);

    void deleteDebate(Long id);
}
