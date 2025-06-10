package com.epam.trainerworkloadservice.controller;

import com.epam.trainerworkloadservice.dto.TrainerWorkloadDto;
import com.epam.trainerworkloadservice.enums.ActionType;
import com.epam.trainerworkloadservice.security.TestSecurityConfig;
import com.epam.trainerworkloadservice.service.TrainerWorkloadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(TrainerWorkloadController.class)
class TrainerWorkloadControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TrainerWorkloadService trainerWorkloadService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnSuccessMessage_whenWorkloadIsUpdated() throws Exception {
        // given
        TrainerWorkloadDto dto = TrainerWorkloadDto.builder()
                .username("trainer1")
                .actionType(ActionType.ADD)
                .trainingDate(LocalDate.of(2024, 6, 1))
                .trainingDuration(45)
                .firstname("John")
                .lastname("Doe")
                .isActive(true)
                .build();

        // when & then
        mockMvc.perform(post("/trainer-workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Workload has been updated successfully"));

        Mockito.verify(trainerWorkloadService).updateTrainerWorkload(dto);
    }

    @Test
    void shouldReturnBadRequest_whenInvalidPayload() throws Exception {
        // given: missing required fields like username
        String invalidPayload = "{}";

        // when & then
        mockMvc.perform(post("/trainer-workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }
}
