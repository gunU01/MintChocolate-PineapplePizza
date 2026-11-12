package com.mintchocolate.pineapplepizza.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(max = 20, message = "닉네임은 20자 이하로 입력해 주세요.")
    private String nickname;

    @NotBlank(message = "댓글 내용을 입력해 주세요.")
    @Size(max = 1000, message = "댓글 내용은 1000자 이하로 입력해 주세요.")
    private String content;
}
