package com.mintchocolate.pineapplepizza.vote.repository;

import com.mintchocolate.pineapplepizza.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByDebateIdAndVoterKey(Long debateId, String voterKey);

    boolean existsByDebateIdAndVoterKey(Long debateId, String voterKey);
}
