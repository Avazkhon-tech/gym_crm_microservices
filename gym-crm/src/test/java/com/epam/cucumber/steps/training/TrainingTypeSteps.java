package com.epam.cucumber.steps.training;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.model.TrainingType;
import com.epam.repository.TrainingTypeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class TrainingTypeSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private ResponseSteps responseSteps;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult result;
    private List<TrainingType> responseList;


    @When("I send a GET request to fetch training types from {string}")
    public void i_send_a_get_request(String path) throws Exception {
        result = mockMvc.perform(get(path).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        responseSteps.setResult(result);

        String content = result.getResponse().getContentAsString();
        responseList = objectMapper.readValue(content, new TypeReference<>() {});
    }


    @And("the response contains training types")
    public void the_response_contains_training_types() {
        assertThat(responseList.size()).isGreaterThan(0);
    }
}
