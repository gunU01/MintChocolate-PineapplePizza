package com.mintchocolate.pineapplepizza.vote.service;

import com.mintchocolate.pineapplepizza.vote.dto.VoteRequest;
import com.mintchocolate.pineapplepizza.vote.dto.VoteResponse;

public interface VoteService {

    VoteResponse createVote(Long debateId, String voterKey, VoteRequest request);
}
