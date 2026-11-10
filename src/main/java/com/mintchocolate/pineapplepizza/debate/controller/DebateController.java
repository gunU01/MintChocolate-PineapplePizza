package com.mintchocolate.pineapplepizza.debate.controller;

import com.mintchocolate.pineapplepizza.debate.dto.DebateRequest;
import com.mintchocolate.pineapplepizza.debate.dto.DebateResponse;
import com.mintchocolate.pineapplepizza.debate.service.DebateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<DebateResponse> getDebateById(@PathVariable Long id) {
        DebateResponse response = debateService.getDebateById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DebateResponse>> getDebates() {
        List<DebateResponse> responses = debateService.getDebates();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DebateResponse> updateDebate(@PathVariable Long id, @Valid @RequestBody DebateRequest request) {
        DebateResponse response = debateService.updateDebate(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDebate(@PathVariable Long id) {
        debateService.deleteDebate(id);
        return ResponseEntity.noContent().build();
    }
}
