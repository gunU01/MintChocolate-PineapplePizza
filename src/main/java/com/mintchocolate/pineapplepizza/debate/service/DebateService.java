package com.mintchocolate.pineapplepizza.debate.service;

import com.mintchocolate.pineapplepizza.debate.dto.DebatePageRequest;
import com.mintchocolate.pineapplepizza.debate.dto.DebateRequest;
import com.mintchocolate.pineapplepizza.debate.dto.DebateResponse;
import org.springframework.data.domain.Page;

public interface DebateService {

    DebateResponse createDebate(DebateRequest request);

    DebateResponse getDebateById(Long id);

    Page<DebateResponse> getDebates(String keyword, DebatePageRequest request);

    DebateResponse updateDebate(Long id, DebateRequest request);

    void deleteDebate(Long id);
}
