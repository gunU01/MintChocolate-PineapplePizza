package com.mintchocolate.pineapplepizza.comment.service;

import com.mintchocolate.pineapplepizza.comment.dto.CommentRequest;
import com.mintchocolate.pineapplepizza.comment.dto.CommentResponse;
import com.mintchocolate.pineapplepizza.comment.entity.Comment;
import com.mintchocolate.pineapplepizza.comment.repository.CommentRepository;
import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final DebateRepository debateRepository;

    @Override
    @Transactional
    public CommentResponse createComment(Long debateId, CommentRequest request) {
        Debate debate = debateRepository.findById(debateId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Comment comment = new Comment(debate, request.getNickname(), request.getContent());
        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.from(savedComment);
    }
}
