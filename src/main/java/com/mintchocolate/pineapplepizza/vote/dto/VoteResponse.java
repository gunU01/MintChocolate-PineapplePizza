package com.mintchocolate.pineapplepizza.vote.dto;

import com.mintchocolate.pineapplepizza.vote.entity.Vote;
import com.mintchocolate.pineapplepizza.vote.entity.VoteChoice;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class VoteResponse {

    private Long id;
    private Long debateId;
    private VoteChoice choice;
    private LocalDateTime createdAt;

    public static VoteResponse from(Vote vote) {
        return new VoteResponse(
                vote.getId(),
                vote.getDebate().getId(),
                vote.getChoice(),
                vote.getCreatedAt()
        );
    }
}
