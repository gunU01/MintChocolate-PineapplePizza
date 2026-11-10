package com.mintchocolate.pineapplepizza.debate.controller;

import com.mintchocolate.pineapplepizza.debate.dto.DebateRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Test
    void getDebateById() throws Exception {
        // given
        Long debateId = 3L;

        // when
        ResultActions result = mockMvc.perform(get("/api/debates/{id}", debateId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("다들 몇 시간 정도 주무시나요"))
                .andExpect(jsonPath("$.description").value("제곧내"))
                .andExpect(jsonPath("$.optionA").value("7시간 미만"))
                .andExpect(jsonPath("$.optionB").value("7시간 이상"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void getDebateByIdNotFound() throws Exception {
        // given
        Long debateId = 999L;

        // when
        ResultActions result = mockMvc.perform(get("/api/debates/{id}", debateId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void getDebates() throws Exception {
        // given
        Debate debate = new Debate("평생 여름 VS 평생 겨울", "하나만 선택한다면?", "여름", "겨울");
        Debate savedDebate = debateRepository.save(debate);

        // when
        ResultActions result = mockMvc.perform(get("/api/debates"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value(savedDebate.getId().intValue()))
                .andExpect(jsonPath("$[0].title").value("평생 여름 VS 평생 겨울"))
                .andExpect(jsonPath("$[0].description").value("하나만 선택한다면?"))
                .andExpect(jsonPath("$[0].optionA").value("여름"))
                .andExpect(jsonPath("$[0].optionB").value("겨울"))
                .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[2].id").value(2))
                .andExpect(jsonPath("$[3].id").value(1));
    }

    @Test
    void getDebatesWhenEmpty() throws Exception {
        // given
        debateRepository.deleteAll();

        // when
        ResultActions result = mockMvc.perform(get("/api/debates"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void updateDebate() throws Exception {
        // given
        Long debateId = 3L;
        DebateRequest request = new DebateRequest("수면 시간은 몇 시간이 적당할까요", "평소 수면 시간을 골라 주세요.", "6시간 미만", "6시간 이상");
        JsonMapper jsonMapper = JsonMapper.builder().build();
        String requestBody = jsonMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put("/api/debates/{id}", debateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.optionA").value(request.getOptionA()))
                .andExpect(jsonPath("$.optionB").value(request.getOptionB()));
        Debate updatedDebate = debateRepository.findById(debateId).orElseThrow();
        assertThat(updatedDebate.getTitle()).isEqualTo(request.getTitle());
    }

    @Test
    void updateDebateWithBlankTitle() throws Exception {
        // given
        Long debateId = 3L;
        DebateRequest request = new DebateRequest(" ", "수정한 설명", "6시간 미만", "6시간 이상");
        JsonMapper jsonMapper = JsonMapper.builder().build();
        String requestBody = jsonMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put("/api/debates/{id}", debateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
        Debate debate = debateRepository.findById(debateId).orElseThrow();
        assertThat(debate.getTitle()).isEqualTo("다들 몇 시간 정도 주무시나요");
        assertThat(debate.getDescription()).isEqualTo("제곧내");
        assertThat(debate.getOptionA()).isEqualTo("7시간 미만");
        assertThat(debate.getOptionB()).isEqualTo("7시간 이상");
    }

    @Test
    void updateDebateNotFound() throws Exception {
        // given
        Long debateId = 999L;
        DebateRequest request = new DebateRequest("수면 시간은 몇 시간이 적당할까요", "평소 수면 시간을 골라 주세요.", "6시간 미만", "6시간 이상");
        JsonMapper jsonMapper = JsonMapper.builder().build();
        String requestBody = jsonMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put("/api/debates/{id}", debateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void deleteDebate() throws Exception {
        // given
        Long debateId = 3L;
        long beforeCount = debateRepository.count();

        // when
        ResultActions result = mockMvc.perform(delete("/api/debates/{id}", debateId));

        // then
        result.andExpect(status().isNoContent())
                .andExpect(content().string(""));
        assertThat(debateRepository.findById(debateId)).isEmpty();
        assertThat(debateRepository.count()).isEqualTo(beforeCount - 1);
    }

    @Test
    void deleteDebateNotFound() throws Exception {
        // given
        Long debateId = 999L;
        long beforeCount = debateRepository.count();

        // when
        ResultActions result = mockMvc.perform(delete("/api/debates/{id}", debateId));

        // then
        result.andExpect(status().isNotFound());
        assertThat(debateRepository.count()).isEqualTo(beforeCount);
    }
}
