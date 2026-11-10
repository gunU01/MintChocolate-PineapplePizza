package com.mintchocolate.pineapplepizza.debate.repository;

import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebateRepository extends JpaRepository<Debate, Long> {

    List<Debate> findAllByOrderByCreatedAtDescIdDesc();
}
