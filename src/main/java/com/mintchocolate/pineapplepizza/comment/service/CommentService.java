package com.mintchocolate.pineapplepizza.comment.service;

import com.mintchocolate.pineapplepizza.comment.dto.CommentRequest;
import com.mintchocolate.pineapplepizza.comment.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(Long debateId, CommentRequest request);

    CommentResponse getCommentById(Long id);

    List<CommentResponse> getCommentsByDebateId(Long debateId);

    CommentResponse updateComment(Long id, CommentRequest request);

    void deleteComment(Long id);
}
