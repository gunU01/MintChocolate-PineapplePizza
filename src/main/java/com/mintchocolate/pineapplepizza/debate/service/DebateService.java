package com.mintchocolate.pineapplepizza.debate.service;

import com.mintchocolate.pineapplepizza.debate.dto.DebateRequest;
import com.mintchocolate.pineapplepizza.debate.dto.DebateResponse;

public interface DebateService {

    DebateResponse createDebate(DebateRequest request);
}
