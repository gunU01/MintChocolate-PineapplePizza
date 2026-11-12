package com.mintchocolate.pineapplepizza.comment.controller;

import com.mintchocolate.pineapplepizza.comment.dto.CommentRequest;
import com.mintchocolate.pineapplepizza.comment.dto.CommentResponse;
import com.mintchocolate.pineapplepizza.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/debates/{id}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long id, @Valid @RequestBody CommentRequest request) {
        CommentResponse response = commentService.createComment(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
