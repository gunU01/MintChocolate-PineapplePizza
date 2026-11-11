package com.mintchocolate.pineapplepizza.comment.repository;

import com.mintchocolate.pineapplepizza.comment.entity.Comment;
import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private DebateRepository debateRepository;

    @Test
    void saveAndFindById() {
        // given
        Debate debate = debateRepository.save(
                new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울"));
        Comment comment = new Comment(debate, "석주", "저는 겨울을 선택하겠습니다.");

        // when
        Comment savedComment = commentRepository.save(comment);
        Comment foundComment = commentRepository.findById(savedComment.getId()).orElseThrow();

        // then
        assertThat(foundComment.getId()).isNotNull();
        assertThat(foundComment.getNickname()).isEqualTo("석주");
        assertThat(foundComment.getContent()).isEqualTo("저는 겨울을 선택하겠습니다.");
    }

}
