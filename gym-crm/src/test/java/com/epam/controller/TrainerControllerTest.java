package com.epam.controller;

import com.epam.dto.auth.LoginDto;
import com.epam.dto.trainer.TrainerProfileDto;
import com.epam.dto.trainer.TrainerProfileUpdateDto;
import com.epam.dto.trainer.TrainerRegistrationDto;
import com.epam.security.TestSecurityConfig;
import com.epam.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithMockUser(username = "test")
@WebMvcTest(TrainerController.class)
@Import(TestSecurityConfig.class)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TrainerService trainerService;

    @Test
    void shouldCreateTrainerSuccessfully() throws Exception {
        TrainerRegistrationDto traineeRegistrationDto = Instancio.create(TrainerRegistrationDto.class);
        LoginDto loginDto = Instancio.create(LoginDto.class);

        when(trainerService.createTrainer(any(TrainerRegistrationDto.class))).thenReturn(loginDto);

        mockMvc.perform(post("/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeRegistrationDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnTraineeProfileSuccessfully() throws Exception {
        TrainerProfileDto trainerProfileDto = Instancio.create(TrainerProfileDto.class);

        when(trainerService.getTrainerProfileByUsername(any())).thenReturn(trainerProfileDto);

        mockMvc.perform(get("/trainers/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerProfileDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateTrainerProfileSuccessfully() throws Exception {
        TrainerProfileUpdateDto trainerProfileUpdateDto = Instancio.of(TrainerProfileUpdateDto.class)
                .set(Select.field("username"), "test").create();

        TrainerProfileDto trainerProfileDto = Instancio.create(TrainerProfileDto.class);

        when(trainerService.updateTrainer(trainerProfileUpdateDto)).thenReturn(trainerProfileDto);

        mockMvc.perform(put("/trainers")
                        .content(objectMapper.writeValueAsString(trainerProfileUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateTrainerActiveStatusSuccessfully() throws Exception {
        doNothing().when(trainerService).updateActiveStatus("test", true);

        mockMvc.perform(patch("/trainers/test")
                        .param("isActive", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}