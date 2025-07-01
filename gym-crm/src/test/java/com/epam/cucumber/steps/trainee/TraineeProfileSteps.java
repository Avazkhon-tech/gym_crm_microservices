package com.epam.cucumber.steps.trainee;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.cucumber.steps.SharedMemory;
import com.epam.dto.trainee.TraineeProfileDto;
import com.epam.dto.trainee.TraineeTrainerDto;
import com.epam.repository.TraineeRepository;
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
public class TraineeProfileSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ResponseSteps responseSteps;
    private final TraineeRepository traineeRepository;
    private final SharedMemory sharedMemory;

    @Given("a trainee with username {string} exists")
    public void a_trainee_with_username_exists(String username) {
        sharedMemory.put("traineeUsername", username);
        boolean exists = traineeRepository.existsByUsername(username);
        assertThat(exists).as("Trainee should exist").isTrue();
    }

    @When("the trainee sends a GET request to {string}")
    public void i_send_a_get_request_to(String path) throws Exception {
        MvcResult result = mockMvc.perform(get(path)
                        .with(user(sharedMemory.get("traineeUsername").toString()).roles("TRAINEE"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        responseSteps.setMvcResult(result);
    }

    @Then("the trainee profile is not empty")
    public void the_trainee_profile_username_is() throws UnsupportedEncodingException, JsonProcessingException {
        String bodyJson = responseSteps.getMvcResult().getResponse().getContentAsString();
        TraineeProfileDto traineeProfileDto = objectMapper.readValue(bodyJson, TraineeProfileDto.class);

        assertThat(traineeProfileDto).isNotNull();
        assertThat(traineeProfileDto.firstname()).isNotNull();
        assertThat(traineeProfileDto.lastname()).isNotNull();
        assertThat(traineeProfileDto.dateOfBirth()).isNotNull();
        assertThat(traineeProfileDto.address()).isNotNull();
    }

    @And("the response contains a list of unassigned trainers")
    public void the_response_contains_a_list_of_unassigned_trainers() throws UnsupportedEncodingException, JsonProcessingException {
        String bodyJson = responseSteps.getMvcResult().getResponse().getContentAsString();
        List<TraineeTrainerDto> trainers = objectMapper.readValue(bodyJson, new TypeReference<>(){});
        assertThat(trainers).isNotNull();
        assertFalse(trainers.isEmpty());
    }
}

