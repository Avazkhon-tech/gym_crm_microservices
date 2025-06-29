package com.epam.cucumber.steps.trainee;

import com.epam.cucumber.steps.CommonSteps;
import com.epam.dto.trainee.TraineeProfileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class TraineeProfileSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommonSteps commonSteps;

    private String username;
    private MvcResult result;
    private TraineeProfileDto profileDto;

    @Given("a trainee with username {string} exists")
    public void a_trainee_with_username_exists(String username) {
        this.username = username;
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String path) throws Exception {
        result = mockMvc.perform(get(path)
                        .with(user(username).roles("TRAINEE"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        commonSteps.setResult(result);

        String json = result.getResponse().getContentAsString();
        if (!json.isBlank()) {
            profileDto = objectMapper.readValue(json, TraineeProfileDto.class);
        }
    }

    @Then("the trainee profile is not empty")
    public void the_trainee_profile_username_is() {
        assertThat(profileDto).isNotNull();
        assertThat(profileDto.firstname()).isNotNull();
        assertThat(profileDto.lastname()).isNotNull();
    }

    @Then("the trainee profile is empty")
    public void theTraineeProfileIsEmpty() {
        assertThat(profileDto).isNotNull();
        assertThat(profileDto.firstname()).isNull();
        assertThat(profileDto.lastname()).isNull();
    }
}

