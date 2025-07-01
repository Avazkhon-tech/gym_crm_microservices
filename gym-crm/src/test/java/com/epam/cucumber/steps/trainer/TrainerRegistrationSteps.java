package com.epam.cucumber.steps.trainer;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.cucumber.steps.SharedMemory;
import com.epam.dto.auth.LoginDto;
import com.epam.dto.trainer.TrainerRegistrationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RequiredArgsConstructor
public class TrainerRegistrationSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ResponseSteps responseSteps;
    private final SharedMemory sharedMemory;

    private TrainerRegistrationDto traineeDto;

    private LoginDto loginDto;

    @Given("a trainer with name {string} and surname {string} exists")
    public void a_trainee_named_born_on_living_at(String firstname, String lastname) {
        traineeDto = TrainerRegistrationDto.builder()
                .firstname(firstname)
                .lastname(lastname)
                .specializationId(1)
                .build();
    }

    @When("the trainer registers at {string}")
    public void the_trainee_registers_at(String path) throws Exception {
        String requestJson = objectMapper.writeValueAsString(traineeDto);

        MvcResult result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andReturn();

        responseSteps.setMvcResult(result);
    }

    @And("the response body for trainer registration contains a username and password")
    public void the_response_body_for_trainer_registration_contains_a_username_and_password() throws JsonProcessingException, UnsupportedEncodingException {
        String responseBody = responseSteps.getMvcResult().getResponse().getContentAsString();
        loginDto = objectMapper.readValue(responseBody, LoginDto.class);
        assertThat(loginDto).isNotNull();
        assertThat(loginDto.username()).isNotNull();
        assertThat(loginDto.password()).isNotNull();
    }
}
