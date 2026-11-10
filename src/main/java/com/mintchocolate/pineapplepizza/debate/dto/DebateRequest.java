package com.mintchocolate.pineapplepizza.debate.dto;

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
public class DebateRequest {

    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해 주세요.")
    private String title;

    @Size(max = 1000, message = "설명은 1000자 이하로 입력해 주세요.")
    private String description;

    @NotBlank(message = "선택지 A를 입력해 주세요.")
    @Size(max = 50, message = "선택지 A는 50자 이하로 입력해 주세요.")
    private String optionA;

    @NotBlank(message = "선택지 B를 입력해 주세요.")
    @Size(max = 50, message = "선택지 B는 50자 이하로 입력해 주세요.")
    private String optionB;
}
