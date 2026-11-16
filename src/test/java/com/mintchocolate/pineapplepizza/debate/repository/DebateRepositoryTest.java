package com.mintchocolate.pineapplepizza.debate.repository;

import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DebateRepositoryTest {

    @Autowired
    private DebateRepository debateRepository;

    @Test
    void save() {
        // given
        Debate debate = new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울");

        // when
        Debate savedDebate = debateRepository.save(debate);

        // then
        assertThat(savedDebate.getId()).isNotNull();
        assertThat(savedDebate.getTitle()).isEqualTo("평생 여름 VS 평생 겨울");
        assertThat(savedDebate.getDescription()).isEqualTo("하나만 선택한다면?");
        assertThat(savedDebate.getOptionA()).isEqualTo("여름");
        assertThat(savedDebate.getOptionB()).isEqualTo("겨울");
        assertThat(savedDebate.getCreatedAt()).isNotNull();
    }

    @Test
    void findById() {
        // given
        Debate debate = new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울");
        Debate savedDebate = debateRepository.save(debate);

        // when
        Debate foundDebate = debateRepository.findById(savedDebate.getId()).orElseThrow();

        // then
        assertThat(foundDebate.getId()).isEqualTo(savedDebate.getId());
        assertThat(foundDebate.getTitle()).isEqualTo("평생 여름 VS 평생 겨울");
    }

    @Test
    void findAll() {
        // when
        List<Debate> debates = debateRepository.findAll();
        Debate firstDebate = debateRepository.findById(1L).orElseThrow();

        // then
        assertThat(debates).hasSize(3);
        assertThat(firstDebate.getTitle()).isEqualTo("과거로 돌아가는게 로또보다 가치가 있을까요");
        assertThat(firstDebate.getOptionA()).isEqualTo("7살로 돌아가기");
        assertThat(firstDebate.getOptionB()).isEqualTo("이번 주 로또 당첨되기");
        assertThat(firstDebate.getCreatedAt()).isNotNull();
    }

    @Test
    void findByTitleContainingIgnoreCase() {
        // given
        Debate firstDebate = debateRepository.save(new Debate("Java VS Python", "언어 선택", "Java", "Python"));
        Debate secondDebate = debateRepository.save(new Debate("JAVA VS Kotlin", "언어 선택", "Java", "Kotlin"));
        debateRepository.save(new Debate("프로그래밍 언어 선택", "Java도 후보입니다", "C", "Python"));

        // when
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        Page<Debate> debates = debateRepository.findByTitleContainingIgnoreCase("java", pageable);

        // then
        assertThat(debates.getTotalElements()).isEqualTo(2);
        assertThat(debates.getContent()).hasSize(2);
        assertThat(debates.getContent().get(0).getId()).isEqualTo(secondDebate.getId());
        assertThat(debates.getContent().get(1).getId()).isEqualTo(firstDebate.getId());
    }

    @Test
    void findByIdNotFound() {
        // given
        Long debateId = 999L;

        // when, then
        assertThat(debateRepository.findById(debateId)).isEmpty();
    }
}
