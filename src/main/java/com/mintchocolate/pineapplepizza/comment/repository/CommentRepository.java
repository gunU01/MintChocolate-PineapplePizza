package com.mintchocolate.pineapplepizza.comment.repository;

import com.mintchocolate.pineapplepizza.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteByDebateId(Long debateId);
    List<Comment> findByDebateIdOrderByCreatedAtDescIdDesc(Long debateId);
}
