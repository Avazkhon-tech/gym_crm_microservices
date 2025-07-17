package com.epam.cucumber.steps.training;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.cucumber.steps.SharedMemory;
import com.epam.dto.training.TrainingCreateDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RequiredArgsConstructor
public class CreateTrainingSessionSteps {

    private final MockMvc mockMvc;
    private final ResponseSteps responseSteps;
    private final SharedMemory sharedMemory;
    private final ObjectMapper objectMapper;
    private TrainingCreateDto trainingCreateDto;

    @Given("a training session with valid details")
    public void a_training_session_with_valid_details() {
       trainingCreateDto = TrainingCreateDto.builder()
                .traineeUsername("aziz.murodov")
                .trainerUsername("sardor.tursunov")
                .trainingName("Training session for aziz.muradov")
                .trainingDate(LocalDate.now().plusDays(1))
                .trainingDurationMinutes(400)
                .build();
    }

    @When("the client sends a POST request to {string}")
    public void the_client_sends_a_post_request(String path) throws Exception {
        String requestBody = objectMapper.writeValueAsString(trainingCreateDto);

        MvcResult result = mockMvc.perform(post(path)
                        .with(user("aziz.murodov"))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        responseSteps.setMvcResult(result);
    }
}
