package com.epam.cucumber.steps.trainee;

import com.epam.cucumber.steps.CommonSteps;
import com.epam.dto.auth.LoginDto;
import com.epam.dto.trainee.TrainerRegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class TraineeRegistrationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommonSteps commonSteps;

    private TrainerRegistrationDto traineeDto;
    private MvcResult result;
    private LoginDto loginDto;

    @Given("a trainee named {string} {string} born on {string} living at {string}")
    public void a_trainee_named_born_on_living_at(String firstname, String lastname, String dob, String address) {
        traineeDto = TrainerRegistrationDto.builder()
                .firstname(firstname)
                .lastname(lastname)
                .address(address)
                .dateOfBirth(LocalDate.parse(dob))
                .build();
    }

    @When("the trainee registers at {string}")
    public void the_trainee_registers_at(String path) throws Exception {
        String requestJson = objectMapper.writeValueAsString(traineeDto);

        result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andReturn();

        commonSteps.setResult(result);

        String responseBody = result.getResponse().getContentAsString();
        loginDto = objectMapper.readValue(responseBody, LoginDto.class);
    }

    @And("the response body contains a username and password")
    public void the_response_body_contains_username_and_password() {
        assertThat(loginDto).isNotNull();
        assertThat(loginDto.username()).isNotBlank();
        assertThat(loginDto.password()).isNotBlank();
    }
}
