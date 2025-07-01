package com.epam.cucumber.steps.trainee;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.cucumber.steps.SharedMemory;
import com.epam.dto.trainee.TraineeTrainerDto;
import com.epam.repository.TraineeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@RequiredArgsConstructor
public class UpdateTraineeTrainersSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ResponseSteps responseSteps;
    private final TraineeRepository traineeRepository;
    private final SharedMemory sharedMemory;
    private List<String> trainersList;


    @Given("a list of unassigned trainers for the trainee exists")
    public void a_list_of_unassigned_trainers_for_the_trainee_exists() throws UnsupportedEncodingException, JsonProcessingException {
        String bodyJson = responseSteps.getMvcResult().getResponse().getContentAsString();
        List<TraineeTrainerDto> trainers = objectMapper.readValue(bodyJson, new TypeReference<>() {
        });
        trainersList = trainers.stream().map(TraineeTrainerDto::username).toList();
    }


    @When("the trainee sends a PUT request to {string}")
    public void the_trainee_sends_a_put_request_to_aziz_muradov_trainers(String path) throws Exception {

        String requestJson = objectMapper.writeValueAsString(trainersList);

        System.out.println(sharedMemory.get("traineeUsername").toString());
        MvcResult result = mockMvc.perform(put(path)
                        .with(user(sharedMemory.get("traineeUsername").toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andReturn();

        responseSteps.setMvcResult(result);
    }
}