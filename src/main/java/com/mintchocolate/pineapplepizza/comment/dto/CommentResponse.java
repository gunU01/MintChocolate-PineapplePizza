package com.mintchocolate.pineapplepizza.comment.dto;

import com.mintchocolate.pineapplepizza.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private Long debateId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getDebate().getId(),
                comment.getNickname(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
