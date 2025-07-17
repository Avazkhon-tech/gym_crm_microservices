package com.epam.cucumber.steps.trainee;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.cucumber.steps.SharedMemory;
import com.epam.repository.TraineeRepository;
import io.cucumber.java.en.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@RequiredArgsConstructor
public class DeleteTraineeSteps {

    private final MockMvc mockMvc;
    private final ResponseSteps responseSteps;
    private final TraineeRepository traineeRepository;
    private final SharedMemory sharedMemory;

    @And("the trainee sends a DELETE request to {string}")
    public void i_send_a_delete_request_to(String path) throws Exception {
        MvcResult result = mockMvc.perform(delete(path)
                        .with(user(sharedMemory.get("traineeUsername").toString()).roles("TRAINEE"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        responseSteps.setMvcResult(result);
    }

    @But("the trainee with username {string} should not exist")
    public void trainee_should_not_exist(String username) {
        boolean exists = traineeRepository.existsByUsername(username);
        assertThat(exists).as("Trainee should not exist after deletion").isFalse();
    }
}
