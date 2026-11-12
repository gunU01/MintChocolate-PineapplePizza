package com.mintchocolate.pineapplepizza.comment.service;

import com.mintchocolate.pineapplepizza.comment.dto.CommentRequest;
import com.mintchocolate.pineapplepizza.comment.dto.CommentResponse;

public interface CommentService {

    CommentResponse createComment(Long debateId, CommentRequest request);
}
