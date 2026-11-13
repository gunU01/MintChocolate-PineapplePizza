package com.mintchocolate.pineapplepizza.vote.controller;

import com.mintchocolate.pineapplepizza.vote.dto.VoteRequest;
import com.mintchocolate.pineapplepizza.vote.dto.VoteResponse;
import com.mintchocolate.pineapplepizza.vote.service.VoteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debates/{debateId}/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteResponse> createVote(
            @PathVariable Long debateId,
            @RequestHeader("X-Voter-Key") @NotBlank @Size(max = 100) String voterKey,
            @Valid @RequestBody VoteRequest request) {
        VoteResponse response = voteService.createVote(debateId, voterKey, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
