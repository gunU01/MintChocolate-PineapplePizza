package com.mintchocolate.pineapplepizza.debate.controller;

import com.mintchocolate.pineapplepizza.debate.dto.DebateRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DebateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DebateRepository debateRepository;

    @Test
    void createDebate() throws Exception {
        // given
        DebateRequest request = new DebateRequest("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울");
        JsonMapper jsonMapper = JsonMapper.builder().build();
        String requestBody = jsonMapper.writeValueAsString(request);
        long beforeCount = debateRepository.count();

        // when
        ResultActions result = mockMvc.perform(post("/api/debates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("평생 여름 VS 평생 겨울"))
                .andExpect(jsonPath("$.description").value("하나만 선택한다면?"))
                .andExpect(jsonPath("$.optionA").value("여름"))
                .andExpect(jsonPath("$.optionB").value("겨울"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
        assertThat(debateRepository.count()).isEqualTo(beforeCount + 1);
    }

    @Test
    void createDebateWithBlankTitle() throws Exception {
        // given
        DebateRequest request = new DebateRequest(" ", "설명", "여름", "겨울");
        JsonMapper jsonMapper = JsonMapper.builder().build();
        String requestBody = jsonMapper.writeValueAsString(request);
        long beforeCount = debateRepository.count();

        // when
        ResultActions result = mockMvc.perform(post("/api/debates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
        assertThat(debateRepository.count()).isEqualTo(beforeCount);
    }

    @Test
    void createDebateWithLongTitle() throws Exception {
        // given
        DebateRequest request = new DebateRequest("가".repeat(101), "설명", "여름", "겨울");
        JsonMapper jsonMapper = JsonMapper.builder().build();
        String requestBody = jsonMapper.writeValueAsString(request);
        long beforeCount = debateRepository.count();

        // when
        ResultActions result = mockMvc.perform(post("/api/debates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
        assertThat(debateRepository.count()).isEqualTo(beforeCount);
    }
}
