package com.epam.cucumber.steps.auth;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.dto.auth.RefreshTokenRequest;
import com.epam.dto.auth.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class RefreshTokenStep {

    private final ObjectMapper objectMapper;
    private final ResponseSteps responseSteps;
    private final MockMvc mockMvc;

    public RefreshTokenStep(ObjectMapper objectMapper, ResponseSteps responseSteps, MockMvc mockMvc) {
        this.objectMapper = objectMapper;
        this.responseSteps = responseSteps;
        this.mockMvc = mockMvc;
    }

    @When("the user {string} posts the refreshToken to {string}")
    public void theUserPostsTheRefreshTokenTo(String username, String path) throws Exception {
        String bodyJson = responseSteps.getResult().getResponse().getContentAsString();

        Token token = objectMapper.readValue(bodyJson, Token.class);
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(token.refreshToken());
        String refreshToken = objectMapper.writeValueAsString(refreshTokenRequest);


        MvcResult result = mockMvc.perform(post(path)
                        .with(user(username))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshToken))
                .andReturn();

        responseSteps.setResult(result);
    }
}
