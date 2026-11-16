package com.mintchocolate.pineapplepizza.comment.controller;

import com.mintchocolate.pineapplepizza.comment.entity.Comment;
import com.mintchocolate.pineapplepizza.comment.repository.CommentRepository;
import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
