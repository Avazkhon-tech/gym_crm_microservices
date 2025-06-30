package com.epam.cucumber.steps.trainer;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.dto.trainer.TrainerProfileDto;
import com.epam.dto.trainer.TrainerProfileUpdateDto;
import com.epam.repository.TrainerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class TrainerUpdateProfileSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResponseSteps responseSteps;

    private String username;
    private TrainerProfileUpdateDto updateDto;
    private TrainerProfileDto responseDto;
    private MvcResult result;


    @Given("a trainer to update with username {string} exists")
    public void a_trainer_to_update_exists(String username) {
        this.username = username;
        assertThat(trainerRepository.existsByUsername(username)).isTrue();
    }

    @When("trainer updates their profile with new details")
    public void trainer_updates_profile() throws Exception {
        updateDto = TrainerProfileUpdateDto.builder()
                .username(username)
                .firstname("Azam")
                .lastname("Murodjonov")
                .specializationId(1)
                .isActive(true)
                .build();

        String json = objectMapper.writeValueAsString(updateDto);

        result = mockMvc.perform(put("/trainers")
                        .with(user(username).roles("TRAINER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        responseSteps.setResult(result);

        String body = result.getResponse().getContentAsString();
        responseDto = objectMapper.readValue(body, TrainerProfileDto.class);
    }

    @And("the response for trainer profile update request contains updated info")
    public void the_response_contains_updated_info() {
        assertThat(responseDto).isNotNull();
        assertEquals(updateDto.firstname(), responseDto.firstname());
        assertEquals(updateDto.lastname(), responseDto.lastname());
        assertEquals(updateDto.isActive(), responseDto.isActive());
    }
}
