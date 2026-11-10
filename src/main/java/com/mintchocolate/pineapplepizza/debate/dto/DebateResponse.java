package com.mintchocolate.pineapplepizza.debate.dto;

import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DebateResponse {

    private Long id;
    private String title;
    private String description;
    private String optionA;
    private String optionB;
    private LocalDateTime createdAt;

    public static DebateResponse from(Debate debate) {
        return new DebateResponse(
                debate.getId(),
                debate.getTitle(),
                debate.getDescription(),
                debate.getOptionA(),
                debate.getOptionB(),
                debate.getCreatedAt()
        );
    }
}
