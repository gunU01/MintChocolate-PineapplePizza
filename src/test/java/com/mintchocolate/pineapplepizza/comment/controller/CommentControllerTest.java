package com.mintchocolate.pineapplepizza.comment.controller;

import com.mintchocolate.pineapplepizza.comment.dto.CommentRequest;
import com.mintchocolate.pineapplepizza.comment.entity.Comment;
import com.mintchocolate.pineapplepizza.comment.repository.CommentRepository;
import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private DebateRepository debateRepository;

    @Test
    void getCommentById() throws Exception {
        // given
        Debate debate = debateRepository.save(
                new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울"));
        Comment comment = commentRepository.save(
                new Comment(debate, "석주", "저는 겨울을 선택하겠습니다."));

        // when
        ResultActions result = mockMvc.perform(get("/api/comments/{id}", comment.getId()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("석주"))
                .andExpect(jsonPath("$.content").value("저는 겨울을 선택하겠습니다."));
    }

    @Test
    void getCommentsByDebateId() throws Exception {
        // given
        Debate debate = debateRepository.save(
                new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울"));
        commentRepository.save(new Comment(debate, "석주", "첫 번째 댓글"));
        commentRepository.save(new Comment(debate, "석주", "두 번째 댓글"));

        // when
        ResultActions result = mockMvc.perform(get("/api/debates/{id}/comments", debate.getId()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("두 번째 댓글"))
                .andExpect(jsonPath("$[1].content").value("첫 번째 댓글"));
    }

    @Test
    void deleteComment() throws Exception {
        // given
        Debate debate = debateRepository.save(
                new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울"));
        Comment comment = commentRepository.save(new Comment(debate, "석주", "삭제할 댓글"));
        Comment otherComment = commentRepository.save(new Comment(debate, "다른 작성자", "남겨둘 댓글"));

        // when
        ResultActions result = mockMvc.perform(delete("/api/comments/{id}", comment.getId()));

        // then
        result.andExpect(status().isNoContent())
                .andExpect(content().string(""));
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
        assertThat(debateRepository.existsById(debate.getId())).isTrue();
        assertThat(commentRepository.existsById(otherComment.getId())).isTrue();
    }
}
