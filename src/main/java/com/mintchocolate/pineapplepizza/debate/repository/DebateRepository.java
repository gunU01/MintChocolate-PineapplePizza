package com.mintchocolate.pineapplepizza.debate.repository;

import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebateRepository extends JpaRepository<Debate, Long> {

    Page<Debate> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
