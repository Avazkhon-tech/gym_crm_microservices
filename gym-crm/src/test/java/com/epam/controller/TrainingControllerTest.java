package com.epam.controller;

import com.epam.dto.trainee.TraineeTrainingFilter;
import com.epam.dto.trainer.TrainerTrainingFilter;
import com.epam.dto.tranining.TraineeTrainingDto;
import com.epam.dto.tranining.TrainerTrainingDto;
import com.epam.dto.tranining.TrainingCreateDto;
import com.epam.security.TestSecurityConfig;
import com.epam.service.TrainingService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithMockUser(username = "test")
@WebMvcTest(TrainingController.class)
@Import(TestSecurityConfig.class)
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TrainingService trainingService;

    @Test
    void shouldCreateTrainingSuccessfully() throws Exception {

        TrainingCreateDto trainingCreateDto = Instancio.of(TrainingCreateDto.class)
                .set(Select.field(TrainingCreateDto::trainingDate), LocalDate.now().plusYears(10))
                .set(Select.field(TrainingCreateDto::trainingDurationMinutes), 60)
                .create();

        doNothing().when(trainingService).createTraining(any(TrainingCreateDto.class));

        mockMvc.perform(post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingCreateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetTrainerTrainingsSuccessfully() throws Exception {
        List<TrainerTrainingDto> trainings = Instancio.createList(TrainerTrainingDto.class);

        when(trainingService.getTrainerTrainingsByCriteria(any(TrainerTrainingFilter.class)))
                .thenReturn(trainings);

        mockMvc.perform(get("/trainings/trainer/test")
                        .param("fromDate", "2025-04-01")
                        .param("toDate", "2025-04-30")
                        .param("traineeName", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(trainings.size()));
    }


    @Test
    void shouldGetTraineeTrainingsSuccessfully() throws Exception {
        List<TraineeTrainingDto> trainings = Instancio.createList(TraineeTrainingDto.class);

        when(trainingService.getTraineeTrainingsByCriteria(any(TraineeTrainingFilter.class)))
                .thenReturn(trainings);

        mockMvc.perform(get("/trainings/trainee/test")
                        .param("fromDate", "2025-04-01")
                        .param("toDate", "2025-04-07")
                        .param("trainerName", "Anna"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(trainings.size()));
    }



}


















