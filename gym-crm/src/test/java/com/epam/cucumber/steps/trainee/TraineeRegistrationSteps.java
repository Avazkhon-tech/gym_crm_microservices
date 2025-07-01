package com.epam.cucumber.steps.trainee;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.dto.auth.LoginDto;
import com.epam.dto.trainee.TrainerRegistrationDto;
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
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RequiredArgsConstructor
public class TraineeRegistrationSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ResponseSteps responseSteps;

    private TrainerRegistrationDto traineeDto;

    @Given("a trainee named {string} {string} born on {string} living at {string}")
    public void a_trainee_named_born_on_living_at(String firstname, String lastname, String dob, String address) {
        traineeDto = TrainerRegistrationDto.builder()
                .firstname(firstname)
                .lastname(lastname)
                .address(address)
                .dateOfBirth(!dob.isBlank() ? LocalDate.parse(dob) : null)
                .build();
    }

    @When("the trainee registers at {string}")
    public void the_trainee_registers_at(String path) throws Exception {
        String requestJson = objectMapper.writeValueAsString(traineeDto);

        MvcResult result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andReturn();

        responseSteps.setMvcResult(result);

    }

    @And("the response body should contain a username and password")
    public void the_response_body_contains_username_and_password() throws JsonProcessingException, UnsupportedEncodingException {
        String responseBody = responseSteps.getMvcResult().getResponse().getContentAsString();

        LoginDto loginDto = objectMapper.readValue(responseBody, LoginDto.class);

        assertThat(loginDto).isNotNull();
        assertThat(loginDto.username()).isNotBlank();
        assertThat(loginDto.password()).isNotBlank();
    }
}
