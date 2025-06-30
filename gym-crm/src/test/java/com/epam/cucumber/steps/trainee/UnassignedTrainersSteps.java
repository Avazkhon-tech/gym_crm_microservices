package com.epam.cucumber.steps.trainee;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.dto.trainee.TraineeTrainerDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class UnassignedTrainersSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResponseSteps responseSteps;

    private MvcResult result;

    private String username;

    private List<TraineeTrainerDto> trainers;

    @Given("I am a trainee with username {string}")
    public void iAmATraineeWithUsername(String username) {
        this.username = username;
    }

    @When("I want to get unassigned trainers for a trainee from {string}")
    public void i_want_to_get_unassigned_trainers_for_a_trainee(String path) throws Exception {
        result = mockMvc.perform(get(path)
                        .with(user(username).roles("TRAINEE"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        responseSteps.setResult(result);
        if (!json.isBlank()) {
            trainers = objectMapper.readValue(json, new TypeReference<>() {});
        }
    }

    @And("the response contains a list of unassigned trainers")
    public void the_response_contains_a_list_of_unassigned_trainers() throws Exception {
        assertThat(trainers).isNotNull();
        assertFalse(trainers.isEmpty());
    }
}
