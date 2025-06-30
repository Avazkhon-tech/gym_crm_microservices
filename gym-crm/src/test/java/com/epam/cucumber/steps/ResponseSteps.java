package com.epam.cucumber.steps;

import com.epam.dto.response.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseSteps {

    private MvcResult result;

    private final ObjectMapper objectMapper;

    public ResponseSteps(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setResult(MvcResult result) {
        this.result= result;
    }

    public MvcResult getResult() {
        return result;
    }

    @Then("the response status should be {int}")
    public void the_response_status_is(int statusCode) {
        assertNotNull(result.getResponse(), "Response must not be null");
        assertEquals(statusCode, result.getResponse().getStatus());
    }

    @And("the body should contain the message {string}")
    public void the_body_contains_a_message(String expectedMessage) throws Exception {
        String bodyJson = result.getResponse().getContentAsString();
        ResponseMessage message = objectMapper.readValue(bodyJson, ResponseMessage.class);

        assertThat(message).isNotNull();
        assertEquals(expectedMessage, message.message());
    }

    @And("the body contains contains a list of errors")
    public void theBodyContainsContainsAListOfErrors() throws Exception {
        String contentAsString = result.getResponse().getContentAsString();
        assertThat(contentAsString).isNotBlank();
        List<?> list = objectMapper.readValue(contentAsString, List.class);
        assertThat(list).isNotEmpty();
    }
}