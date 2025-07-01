package com.epam.cucumber.steps.trainee;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.cucumber.steps.SharedMemory;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RequiredArgsConstructor
public class UpdateTraineeActiveStatusSteps {

    private final MockMvc mockMvc;
    private final ResponseSteps responseSteps;
    private final SharedMemory sharedMemory;

    @When("the trainee sends a PATCH request to {string}")
    public void the_trainee_sends_a_patch_request(String path) throws Exception {

        System.out.println(sharedMemory.get("traineeUsername").toString());
        MvcResult result = mockMvc.perform(patch(path)
                        .with(user(sharedMemory.get("traineeUsername").toString()))
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();

        responseSteps.setMvcResult(result);
    }
}
