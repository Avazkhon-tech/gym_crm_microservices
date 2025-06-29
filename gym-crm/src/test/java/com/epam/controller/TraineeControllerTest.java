package com.epam.controller;

import com.epam.dto.auth.LoginDto;
import com.epam.dto.trainee.TraineeProfileDto;
import com.epam.dto.trainee.TraineeProfileUpdateDto;
import com.epam.dto.trainee.TrainerRegistrationDto;
import com.epam.dto.trainee.TraineeTrainerDto;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.security.TestSecurityConfig;
import com.epam.service.TraineeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithMockUser(username = "test")
@WebMvcTest(TraineeController.class)
@Import(TestSecurityConfig.class)
class TraineeControllerTest {

    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TraineeService traineeService;


    @Test
    void shouldCreateTraineeSuccessfully() throws Exception {
        TrainerRegistrationDto trainerRegistrationDto = Instancio.of(TrainerRegistrationDto.class)
                .set(Select.field(TrainerRegistrationDto::dateOfBirth), LocalDate.now().minusYears(20))
                .create();
        LoginDto loginDto = Instancio.create(LoginDto.class);

        when(traineeService.createTrainee(any(TrainerRegistrationDto.class))).thenReturn(loginDto);

        mockMvc.perform(post("/trainees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRegistrationDto)))
                .andExpect(status().isOk());
    }


    @Test
    void shouldReturnTraineeProfileSuccessfully() throws Exception {
        TraineeProfileDto traineeProfileDto = Instancio.of(TraineeProfileDto.class)
                .set(Select.field(TraineeProfileDto::dateOfBirth), LocalDate.now().minusYears(20)).create();

        when(traineeService.getTraineeProfile("test")).thenReturn(traineeProfileDto);

        mockMvc.perform(put("/trainees/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeProfileDto)))
                .andExpect(status().isOk());
    }


    @Nested
    class DeleteTraineeTests {

        @Test
        void shouldDeleteTraineeSuccessfully() throws Exception {
            doNothing().when(traineeService).deleteTrainee("test");

            mockMvc.perform(delete("/trainees/{username}", "test")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldThrowExceptionWhenTraineeDoesNotExists() throws Exception {
            EntityDoesNotExistException entityDoesNotExistException = Instancio.create(EntityDoesNotExistException.class);

            doThrow(entityDoesNotExistException).when(traineeService).deleteTrainee("test");

            mockMvc.perform(delete("/trainees/{username}", "test")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void shouldReturnUnassignedTraineeSuccessfully() throws Exception {
        List<TraineeTrainerDto> traineeTrainerDtoList = Instancio.stream(TraineeTrainerDto.class)
                .limit(10)
                .toList();

        when(traineeService.getActiveUnassignedTrainers("test")).thenReturn(traineeTrainerDtoList);

        mockMvc.perform(get("/trainees/{username}/unassigned-trainers", "test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void shouldUpdateTraineeSuccessfully() throws Exception {

        List<TraineeTrainerDto> list = Instancio.stream(TraineeTrainerDto.class)
                .limit(10)
                .toList();

        List<String> trainerUsernames = list.stream().map(TraineeTrainerDto::username).toList();

        when(traineeService.updateTraineeTrainers("test", trainerUsernames))
                .thenReturn(list);


        mockMvc.perform(put("/trainees/{username}/trainers", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerUsernames)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateTraineeActiveStatusSuccessfully() throws Exception {
        doNothing().when(traineeService).updateActiveStatus("test", true);

        mockMvc.perform(patch("/trainees/{username}", "test")
                        .param("isActive", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateTraineeProfileSuccessfully() throws Exception {
        TraineeProfileUpdateDto traineeProfileUpdateDto = Instancio.of(TraineeProfileUpdateDto.class)
                .set(Select.field(TraineeProfileUpdateDto::dateOfBirth), LocalDate.now().minusYears(20))
                .create();
        TraineeProfileDto traineeProfileDto = Instancio.create(TraineeProfileDto.class);

        when(traineeService.updateTrainee("test", traineeProfileUpdateDto)).thenReturn(traineeProfileDto);

        mockMvc.perform(put("/trainees/{username}", "test")
                        .content(objectMapper.writeValueAsString(traineeProfileUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
























