package com.epam.cucumber.steps.auth;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.dto.auth.CredentialsUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RequiredArgsConstructor
public class UpdatePassword {

    private final MockMvc mockMvc;
    private final ResponseSteps responseSteps;
    private final ObjectMapper objectMapper;
    private CredentialsUpdateDto credentialsUpdateDto;


    @Given("a user {string} with current password {string} and new password {string}")
    public void a_user_with_current_password(String username, String password, String newPassword) {
        credentialsUpdateDto = new CredentialsUpdateDto(username, password, newPassword);
    }

    @When("the user sends a change‑password request to {string}")
    public void the_user_sends_a_change_password_request(String path) throws Exception {

        String bodyJson = objectMapper.writeValueAsString(credentialsUpdateDto);

        MvcResult result = mockMvc.perform(put(path)
                        .with(user(credentialsUpdateDto.username()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson))
                .andReturn();

        responseSteps.setMvcResult(result);
    }
}
