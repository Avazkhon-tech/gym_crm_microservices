package com.epam.cucumber.steps.trainee;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.repository.TraineeRepository;
import com.epam.dto.response.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

public class DeleteTraineeSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResponseSteps responseSteps;

    @Autowired
    private TraineeRepository traineeRepository;

    private MvcResult result;
    private ResponseMessage responseMessage;
    private String username;

    @When("Checked for trainee with username {string}, trainee should exist")
    public void trainee_should_exist(String username) {
        this.username = username;
        boolean exists = traineeRepository.existsByUsername(username);
        assertThat(exists).as("Trainee should exist before deletion").isTrue();
    }

    @And("I send a DELETE request to {string}")
    public void i_send_a_delete_request_to(String path) throws Exception {
        result = mockMvc.perform(delete(path)
                        .with(user(username).roles("TRAINEE"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        responseSteps.setResult(result);

        String responseBody = result.getResponse().getContentAsString();
        if (!responseBody.isBlank()) {
            responseMessage = objectMapper.readValue(responseBody, ResponseMessage.class);
        }
    }


    @But("the trainee with username {string} should not exist")
    public void trainee_should_not_exist(String username) {
        boolean exists = traineeRepository.existsByUsername(username);
        assertThat(exists).as("Trainee should not exist after deletion").isFalse();
    }
}
