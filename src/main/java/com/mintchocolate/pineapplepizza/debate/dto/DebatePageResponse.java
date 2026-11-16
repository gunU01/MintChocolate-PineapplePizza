package com.mintchocolate.pineapplepizza.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class DebatePageResponse {

    private List<DebateResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static DebatePageResponse from(Page<DebateResponse> page) {
        return new DebatePageResponse(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
