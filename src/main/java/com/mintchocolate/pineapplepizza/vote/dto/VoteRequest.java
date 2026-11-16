package com.mintchocolate.pineapplepizza.vote.dto;

import com.mintchocolate.pineapplepizza.vote.entity.VoteChoice;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {

    @NotNull(message = "선택지를 골라주세요.")
    private VoteChoice choice;
}
