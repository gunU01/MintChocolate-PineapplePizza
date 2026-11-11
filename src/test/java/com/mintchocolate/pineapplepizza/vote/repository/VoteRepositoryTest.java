package com.mintchocolate.pineapplepizza.vote.repository;

import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import com.mintchocolate.pineapplepizza.vote.entity.Vote;
import com.mintchocolate.pineapplepizza.vote.entity.VoteChoice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class VoteRepositoryTest {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private DebateRepository debateRepository;

    @Test
    void saveAndFindByDebateIdAndVoterKey() {
        // given
        Debate debate = debateRepository.save(
                new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울"));
        Vote vote = new Vote(debate, "voter-a", VoteChoice.A);

        // when
        Vote savedVote = voteRepository.save(vote);
        Vote foundVote = voteRepository.findByDebateIdAndVoterKey(debate.getId(), "voter-a").orElseThrow();

        // then
        assertThat(savedVote.getId()).isNotNull();
        assertThat(foundVote.getId()).isEqualTo(savedVote.getId());
        assertThat(foundVote.getVoterKey()).isEqualTo("voter-a");
        assertThat(foundVote.getChoice()).isEqualTo(VoteChoice.A);
        assertThat(foundVote.getCreatedAt()).isNotNull();
    }

    @Test
    void findByDebateIdAndVoterKeyWithOtherKey() {
        // given
        Debate debate = debateRepository.save(
                new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울"));
        voteRepository.save(new Vote(debate, "voter-a", VoteChoice.A));

        // when, then
        assertThat(voteRepository.findByDebateIdAndVoterKey(debate.getId(), "voter-b")).isEmpty();
        assertThat(voteRepository.existsByDebateIdAndVoterKey(debate.getId(), "voter-a")).isTrue();
        assertThat(voteRepository.existsByDebateIdAndVoterKey(debate.getId(), "voter-b")).isFalse();
    }

    @Test
    void saveDuplicateVoteInSameDebate() {
        // given
        Debate debate = debateRepository.save(
                new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울"));
        voteRepository.saveAndFlush(new Vote(debate, "voter-a", VoteChoice.A));

        // when, then
        assertThatThrownBy(() -> voteRepository.saveAndFlush(new Vote(debate, "voter-a", VoteChoice.B)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void saveSameVoterKeyInDifferentDebates() {
        // given
        Debate summer = debateRepository.save(
                new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울"));
        Debate sleep = debateRepository.save(
                new Debate("아침형 VS 저녁형", "어느 쪽인가요?", "아침형", "저녁형"));

        // when
        voteRepository.saveAndFlush(new Vote(summer, "voter-a", VoteChoice.A));
        voteRepository.saveAndFlush(new Vote(sleep, "voter-a", VoteChoice.B));

        // then
        assertThat(voteRepository.findByDebateIdAndVoterKey(summer.getId(), "voter-a").orElseThrow().getChoice())
                .isEqualTo(VoteChoice.A);
        assertThat(voteRepository.findByDebateIdAndVoterKey(sleep.getId(), "voter-a").orElseThrow().getChoice())
                .isEqualTo(VoteChoice.B);
    }
}
