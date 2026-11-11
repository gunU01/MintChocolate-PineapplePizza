package com.mintchocolate.pineapplepizza.comment.repository;

import com.mintchocolate.pineapplepizza.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
