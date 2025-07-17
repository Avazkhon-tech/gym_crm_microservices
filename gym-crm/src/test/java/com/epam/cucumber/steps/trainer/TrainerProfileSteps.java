package com.epam.cucumber.steps.trainer;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.cucumber.steps.SharedMemory;
import com.epam.dto.trainee.TraineeProfileDto;
import com.epam.dto.trainee.TraineeTrainerDto;
import com.epam.dto.trainer.TrainerProfileDto;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RequiredArgsConstructor
public class TrainerProfileSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ResponseSteps responseSteps;
    private final TrainerRepository trainerRepository;
    private final SharedMemory sharedMemory;


    @Given("a trainer with username {string} exists")
    public void a_trainer_with_username_exists(String username) {
        sharedMemory.put("trainerUsername", username);
        boolean exists = trainerRepository.existsByUsername(username);
        assertThat(exists).as("Trainer should exist").isTrue();
    }

    @When("the trainer sends a GET request to {string}")
    public void the_trainer_sends_a_get_request(String path) throws Exception {
        MvcResult result = mockMvc.perform(get(path)
                        .with(user(sharedMemory.get("trainerUsername").toString()).roles("TRAINER"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        responseSteps.setMvcResult(result);
    }

    @Then("the trainer profile is not empty")
    public void the_trainer_profile_is_not_empty() throws JsonProcessingException, UnsupportedEncodingException {
        String bodyJson = responseSteps.getMvcResult().getResponse().getContentAsString();
        TrainerProfileDto traineeProfileDto = objectMapper.readValue(bodyJson, TrainerProfileDto.class);

        assertThat(traineeProfileDto).isNotNull();
        assertThat(traineeProfileDto.firstname()).isNotNull();
        assertThat(traineeProfileDto.lastname()).isNotNull();
    }
}

