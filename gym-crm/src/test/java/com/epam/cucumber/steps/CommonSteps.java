package com.epam.cucumber.steps;

import io.cucumber.java.en.Then;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;

public class CommonSteps {

    private MvcResult result;

    public void setResult(MvcResult result) {
        this.result= result;
    }

    @Then("the response status is {int}")
    public void the_response_status_is(int statusCode) {
        assertNotNull(result.getResponse(), "Response must not be null");
        assertEquals(statusCode, result.getResponse().getStatus());
    }
}
