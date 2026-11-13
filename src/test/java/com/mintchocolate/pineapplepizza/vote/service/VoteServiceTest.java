package com.mintchocolate.pineapplepizza.vote.service;

import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import com.mintchocolate.pineapplepizza.vote.dto.VoteRequest;
import com.mintchocolate.pineapplepizza.vote.entity.VoteChoice;
import com.mintchocolate.pineapplepizza.vote.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class VoteServiceTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private DebateRepository debateRepository;

    @Test
    void createVoteWithOtherIntegrityViolation() {
        assertThatThrownBy(() -> voteService.createVote(
                1L, "a".repeat(101), new VoteRequest(VoteChoice.A)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void createVoteConcurrently() throws Exception {
        Debate debate = debateRepository.save(new Debate("투표 동시 등록", "테스트", "A", "B"));
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            java.util.concurrent.Callable<Integer> request = () -> {
                ready.countDown();
                start.await();
                try {
                    voteService.createVote(debate.getId(), "concurrent-voter",
                            new VoteRequest(VoteChoice.A));
                    return 201;
                } catch (ResponseStatusException e) {
                    return e.getStatusCode().value();
                }
            };
            Future<Integer> first = executor.submit(request);
            Future<Integer> second = executor.submit(request);
            ready.await();
            start.countDown();

            assertThat(java.util.List.of(first.get(), second.get()))
                    .containsExactlyInAnyOrder(201, 409);
            assertThat(voteRepository.findAll().stream()
                    .filter(vote -> vote.getDebate().getId().equals(debate.getId())).count())
                    .isEqualTo(1);
        } finally {
            voteRepository.findByDebateIdAndVoterKey(debate.getId(), "concurrent-voter")
                    .ifPresent(voteRepository::delete);
            debateRepository.deleteById(debate.getId());
        }
    }
}
