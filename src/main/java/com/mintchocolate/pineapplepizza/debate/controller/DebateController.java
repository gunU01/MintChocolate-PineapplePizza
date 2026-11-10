package com.mintchocolate.pineapplepizza.debate.controller;

import com.mintchocolate.pineapplepizza.debate.dto.DebateRequest;
import com.mintchocolate.pineapplepizza.debate.dto.DebateResponse;
import com.mintchocolate.pineapplepizza.debate.service.DebateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debates")
@RequiredArgsConstructor
public class DebateController {

    private final DebateService debateService;

    @PostMapping
    public ResponseEntity<DebateResponse> createDebate(@Valid @RequestBody DebateRequest request) {
        DebateResponse response = debateService.createDebate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
