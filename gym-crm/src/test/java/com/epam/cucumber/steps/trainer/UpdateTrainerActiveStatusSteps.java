package com.epam.cucumber.steps.trainer;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.cucumber.steps.SharedMemory;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@RequiredArgsConstructor
public class UpdateTrainerActiveStatusSteps {

    private final MockMvc mockMvc;
    private final SharedMemory sharedMemory;
    private final ResponseSteps responseSteps;

    @When("the trainer sends a PATCH request to {string}")
    public void the_trainee_sends_a_patch_request(String path) throws Exception {

        System.out.println(sharedMemory.get("trainerUsername").toString());
        MvcResult result = mockMvc.perform(patch(path)
                .with(user(sharedMemory.get("trainerUsername").toString()))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        responseSteps.setMvcResult(result);
    }
}
