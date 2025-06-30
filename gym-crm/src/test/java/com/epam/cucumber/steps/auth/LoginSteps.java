package com.epam.cucumber.steps.auth;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.dto.auth.LoginDto;
import com.epam.dto.auth.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class LoginSteps {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final ResponseSteps responseSteps;

    public LoginSteps(ResponseSteps responseSteps, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.responseSteps = responseSteps;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    private LoginDto loginDto;


    @Given("a user {string} with password {string}")
    public void a_user_with_password_exists(String username, String rawPassword) {
        this.loginDto = new LoginDto(username, rawPassword);
    }

    @When("the user posts credentials to {string}")
    public void the_client_posts_valid_credentials_to_login(String path) throws Exception {
        String json = objectMapper.writeValueAsString(loginDto);

        MvcResult result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        responseSteps.setResult(result);
    }

    @And("the body contains a non‑blank JWT and refresh token")
    public void the_body_contains_tokens() throws Exception {
        String bodyJson = responseSteps.getResult().getResponse().getContentAsString();
        Token token = objectMapper.readValue(bodyJson, Token.class);

        assertThat(token).isNotNull();
        assertThat(token.accessToken()).isNotBlank();
        assertThat(token.refreshToken()).isNotBlank();
    }
}
