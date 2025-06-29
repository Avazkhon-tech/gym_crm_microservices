package com.epam.cucumber.steps.trainee;

import com.epam.cucumber.steps.CommonSteps;
import com.epam.dto.trainee.TraineeProfileDto;
import com.epam.dto.trainee.TraineeProfileUpdateDto;
import com.epam.model.Trainee;
import com.epam.model.User;
import com.epam.repository.TraineeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class TraineeUpdateProfileSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommonSteps commonSteps;

    @Autowired
    private TraineeRepository traineeRepository;

    private String username;
    private TraineeProfileUpdateDto updateDto;
    private TraineeProfileDto responseDto;
    private MvcResult result;
    private Trainee trainee;

    @Before
    public void setup() {
        trainee = Trainee.builder()
                .user(User.builder()
                        .username("crazy.man")
                        .firstname("crazy")
                        .lastname("man")
                        .isActive(true)
                        .password("password")
                        .build())
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("fergana")
                .build();

        traineeRepository.save(trainee);
    }

    @After
    public void tearDown() {
        traineeRepository.delete(trainee);
    }

    @Given("a trainee to update with username {string} exists")
    public void a_trainer_with_username_exists(String username) {
        this.username = username;
        Boolean existsByUsername = traineeRepository.existsByUsername(username);
        assertThat(existsByUsername).isTrue();
    }


    @When("I update the profile with new details")
    public void i_update_the_profile_with_details() throws Exception {

        updateDto = TraineeProfileUpdateDto
                .builder()
                .address("tashkent")
                .dateOfBirth(LocalDate.now().minusYears(10))
                .firstname("azam")
                .address("somewhere")
                .isActive(true)
                .lastname("lastnamee")
                .build();

        String json = objectMapper.writeValueAsString(updateDto);

        result = mockMvc.perform(put("/trainees/" + username)
                        .with(user(username).roles("TRAINER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        commonSteps.setResult(result);

        String responseBody = result.getResponse().getContentAsString();
        responseDto = objectMapper.readValue(responseBody, TraineeProfileDto.class);
    }

    @And("the response contains updated info")
    public void the_response_contains_updated_fields() {
        assertThat(responseDto).isNotNull();
        assertEquals(updateDto.address(), responseDto.address());
        assertEquals(updateDto.dateOfBirth(), responseDto.dateOfBirth());
        assertEquals(updateDto.firstname(), responseDto.firstname());
        assertEquals(updateDto.lastname(), responseDto.lastname());
    }
}
