package com.mintchocolate.pineapplepizza.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteResultResponse {

    private Long debateId;
    private long totalVotes;
    private long votesA;
    private long votesB;
    private double percentageA;
    private double percentageB;
}
