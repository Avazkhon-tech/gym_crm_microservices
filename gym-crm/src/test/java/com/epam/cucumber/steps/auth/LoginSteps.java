package com.epam.cucumber.steps.auth;

import com.epam.cucumber.steps.CommonSteps;
import com.epam.dto.auth.LoginDto;
import com.epam.dto.auth.Token;
import com.epam.dto.response.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class LoginSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommonSteps commonSteps;

    private LoginDto loginDto;
    private MvcResult result;

    @Given("a user {string} with password {string}")
    public void a_user_with_password_exists(String username, String rawPassword) {
        this.loginDto = new LoginDto(username, rawPassword);
    }

    // Successful login
    @When("the user posts valid credentials to {string}")
    public void the_client_posts_valid_credentials_to_login(String path) throws Exception {
        String json = objectMapper.writeValueAsString(loginDto);

        result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        commonSteps.setResult(result);
    }

    @And("the body contains a non‑blank JWT and refresh token")
    public void the_body_contains_tokens() throws Exception {
        String bodyJson = result.getResponse().getContentAsString();
        Token token = objectMapper.readValue(bodyJson, Token.class);

        assertThat(token).isNotNull();
        assertThat(token.accessToken()).isNotBlank();
        assertThat(token.refreshToken()).isNotBlank();
    }

    // Failed login
    @When("the user posts invalid credentials to {string}")
    public void the_client_posts_invalid_credentials_to_login(String path) throws Exception {
        LoginDto invalidDto = new LoginDto("aziz.murodov", "invalid");
        String json = objectMapper.writeValueAsString(invalidDto);

        result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        commonSteps.setResult(result);
    }

    @And("the body contains contains a message {string}")
    public void the_body_contains_a_message(String expectedMessage) throws Exception {
        String bodyJson = result.getResponse().getContentAsString();
        ResponseMessage message = objectMapper.readValue(bodyJson, ResponseMessage.class);

        assertThat(message).isNotNull();
        assertEquals(expectedMessage, message.message());
    }
}
