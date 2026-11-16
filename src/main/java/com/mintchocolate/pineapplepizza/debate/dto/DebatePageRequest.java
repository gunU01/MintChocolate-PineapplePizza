package com.mintchocolate.pineapplepizza.debate.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebatePageRequest {

    @Min(value = 1, message = "페이지 번호는 1 이상으로 입력해 주세요.")
    private int page = 1;

    @Min(value = 1, message = "조회 개수는 1 이상으로 입력해 주세요.")
    @Max(value = 100, message = "조회 개수는 100 이하로 입력해 주세요.")
    private int size = 10;
}
