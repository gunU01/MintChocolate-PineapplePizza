package com.mintchocolate.pineapplepizza.vote.controller;

import com.mintchocolate.pineapplepizza.debate.entity.Debate;
import com.mintchocolate.pineapplepizza.debate.repository.DebateRepository;
import com.mintchocolate.pineapplepizza.vote.dto.VoteRequest;
import com.mintchocolate.pineapplepizza.vote.entity.Vote;
import com.mintchocolate.pineapplepizza.vote.entity.VoteChoice;
import com.mintchocolate.pineapplepizza.vote.repository.VoteRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private DebateRepository debateRepository;

    @Test
    void createVote() throws Exception {
        // given
        Long debateId = 1L;
        VoteRequest request = new VoteRequest(VoteChoice.A);
        JsonMapper jsonMapper = JsonMapper.builder().build();
        String requestBody = jsonMapper.writeValueAsString(request);
        long beforeCount = voteRepository.count();

        // when
        ResultActions result = mockMvc.perform(post("/api/debates/{id}/votes", debateId)
                .header("X-Voter-Key", "voter-a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.debateId").value(1))
                .andExpect(jsonPath("$.choice").value("A"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.voterKey").doesNotExist());
        assertThat(voteRepository.count()).isEqualTo(beforeCount + 1);
        assertThat(voteRepository.existsByDebateIdAndVoterKey(debateId, "voter-a")).isTrue();
    }

    @Test
    void createVoteDuplicate() throws Exception {
        // given
        Long debateId = 1L;
        Debate debate = debateRepository.findById(debateId).orElseThrow();
        voteRepository.save(new Vote(debate, "voter-a", VoteChoice.A));
        String requestBody = "{\"choice\":\"B\"}";
        long beforeCount = voteRepository.count();

        // when
        ResultActions result = mockMvc.perform(post("/api/debates/{id}/votes", debateId)
                .header("X-Voter-Key", "voter-a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isConflict());
        assertThat(voteRepository.count()).isEqualTo(beforeCount);
        assertThat(voteRepository.findByDebateIdAndVoterKey(debateId, "voter-a").orElseThrow().getChoice())
                .isEqualTo(VoteChoice.A);
    }

    @Test
    void createVoteWithSameKeyInDifferentDebate() throws Exception {
        // given
        Debate debate = debateRepository.findById(1L).orElseThrow();
        voteRepository.save(new Vote(debate, "voter-a", VoteChoice.A));
        String requestBody = "{\"choice\":\"B\"}";

        // when
        ResultActions result = mockMvc.perform(post("/api/debates/{id}/votes", 2L)
                .header("X-Voter-Key", "voter-a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.debateId").value(2))
                .andExpect(jsonPath("$.choice").value("B"));
    }

    @Test
    void createVoteWithoutVoterKey() throws Exception {
        // given
        String requestBody = "{\"choice\":\"A\"}";
        long beforeCount = voteRepository.count();

        // when
        ResultActions result = mockMvc.perform(post("/api/debates/{id}/votes", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
        assertThat(voteRepository.count()).isEqualTo(beforeCount);
    }

    @Test
    void createVoteWithBlankVoterKey() throws Exception {
        // given
        String requestBody = "{\"choice\":\"A\"}";
        long beforeCount = voteRepository.count();

        // when
        ResultActions result = mockMvc.perform(post("/api/debates/{id}/votes", 1L)
                .header("X-Voter-Key", " ")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
        assertThat(voteRepository.count()).isEqualTo(beforeCount);
    }

    @Test
    void createVoteWithInvalidChoice() throws Exception {
        // given
        String requestBody = "{\"choice\":\"C\"}";
        long beforeCount = voteRepository.count();

        // when
        ResultActions result = mockMvc.perform(post("/api/debates/{id}/votes", 1L)
                .header("X-Voter-Key", "voter-a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
        assertThat(voteRepository.count()).isEqualTo(beforeCount);
    }

    @Test
    void createVoteWithoutChoice() throws Exception {
        // given
        String requestBody = "{}";
        long beforeCount = voteRepository.count();

        // when
        ResultActions result = mockMvc.perform(post("/api/debates/{id}/votes", 1L)
                .header("X-Voter-Key", "voter-a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
        assertThat(voteRepository.count()).isEqualTo(beforeCount);
    }

    @Test
    void createVoteWithNonExistentDebate() throws Exception {
        // given
        Long debateId = 999L;
        String requestBody = "{\"choice\":\"A\"}";
        long beforeCount = voteRepository.count();

        // when
        ResultActions result = mockMvc.perform(post("/api/debates/{id}/votes", debateId)
                .header("X-Voter-Key", "voter-a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isNotFound());
        assertThat(voteRepository.count()).isEqualTo(beforeCount);
    }

    @Test
    void getMyVote() throws Exception {
        Debate debate = debateRepository.findById(1L).orElseThrow();
        Vote vote = voteRepository.save(new Vote(debate, "voter-a", VoteChoice.A));
        voteRepository.save(new Vote(debate, "voter-b", VoteChoice.B));

        ResultActions result = mockMvc.perform(get("/api/debates/{id}/votes/me", 1L)
                .header("X-Voter-Key", "voter-a"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vote.getId()))
                .andExpect(jsonPath("$.debateId").value(1))
                .andExpect(jsonPath("$.choice").value("A"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.voterKey").doesNotExist());
    }

    @Test
    void getMyVoteWithoutVote() throws Exception {
        mockMvc.perform(get("/api/debates/{id}/votes/me", 1L)
                        .header("X-Voter-Key", "voter-a"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMyVoteWithOtherKey() throws Exception {
        Debate debate = debateRepository.findById(1L).orElseThrow();
        voteRepository.save(new Vote(debate, "voter-a", VoteChoice.A));

        mockMvc.perform(get("/api/debates/{id}/votes/me", 1L)
                        .header("X-Voter-Key", "voter-b"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMyVoteWithNonExistentDebate() throws Exception {
        mockMvc.perform(get("/api/debates/{id}/votes/me", 999L)
                        .header("X-Voter-Key", "voter-a"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateMyVote() throws Exception {
        // given
        Debate debate = debateRepository.findById(1L).orElseThrow();
        Vote vote = voteRepository.save(new Vote(debate, "voter-a", VoteChoice.A));
        long beforeCount = voteRepository.count();

        // when
        ResultActions result = mockMvc.perform(put("/api/debates/1/votes/me")
                .header("X-Voter-Key", "voter-a")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"choice\":\"B\"}"));

        // then
        result.andExpect(status().isOk());
        assertThat(vote.getChoice()).isEqualTo(VoteChoice.B);
        assertThat(voteRepository.count()).isEqualTo(beforeCount);
    }

    @Test
    void updateMyVoteWithOtherKey() throws Exception {
        // given
        Debate debate = debateRepository.findById(1L).orElseThrow();
        Vote vote = voteRepository.save(new Vote(debate, "voter-a", VoteChoice.A));

        // when
        ResultActions result = mockMvc.perform(put("/api/debates/1/votes/me")
                .header("X-Voter-Key", "voter-b")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"choice\":\"B\"}"));

        // then
        result.andExpect(status().isNotFound());
        assertThat(vote.getChoice()).isEqualTo(VoteChoice.A);
    }

    @Test
    void deleteMyVote() throws Exception {
        // given
        Debate debate = debateRepository.findById(1L).orElseThrow();
        Vote vote = voteRepository.save(new Vote(debate, "voter-a", VoteChoice.A));

        // when
        ResultActions result = mockMvc.perform(delete("/api/debates/1/votes/me")
                .header("X-Voter-Key", "voter-a"));

        // then
        result.andExpect(status().isNoContent());
        assertThat(voteRepository.existsById(vote.getId())).isFalse();
    }

    @Test
    void deleteMyVoteWithOtherKey() throws Exception {
        // given
        Debate debate = debateRepository.findById(1L).orElseThrow();
        Vote vote = voteRepository.save(new Vote(debate, "voter-a", VoteChoice.A));

        // when
        ResultActions result = mockMvc.perform(delete("/api/debates/1/votes/me")
                .header("X-Voter-Key", "voter-b"));

        // then
        result.andExpect(status().isNotFound());
        assertThat(voteRepository.existsById(vote.getId())).isTrue();
    }

    @Test
    void getVoteResults() throws Exception {
        // given
        Debate debate = debateRepository.findById(1L).orElseThrow();
        voteRepository.save(new Vote(debate, "voter-a", VoteChoice.A));
        voteRepository.save(new Vote(debate, "voter-b", VoteChoice.A));
        voteRepository.save(new Vote(debate, "voter-c", VoteChoice.B));

        // when
        ResultActions result = mockMvc.perform(get("/api/debates/1/votes/results"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.debateId").value(1))
                .andExpect(jsonPath("$.totalVotes").value(3))
                .andExpect(jsonPath("$.votesA").value(2))
                .andExpect(jsonPath("$.votesB").value(1))
                .andExpect(jsonPath("$.percentageA").value(66.7))
                .andExpect(jsonPath("$.percentageB").value(33.3));
    }

    @Test
    void getVoteResultsWithoutVotes() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/debates/1/votes/results"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVotes").value(0))
                .andExpect(jsonPath("$.votesA").value(0))
                .andExpect(jsonPath("$.votesB").value(0))
                .andExpect(jsonPath("$.percentageA").value(0.0))
                .andExpect(jsonPath("$.percentageB").value(0.0));
    }
}
