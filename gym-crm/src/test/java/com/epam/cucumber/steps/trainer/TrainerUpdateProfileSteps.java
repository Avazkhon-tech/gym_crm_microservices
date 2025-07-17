package com.epam.cucumber.steps.trainer;

import com.epam.cucumber.steps.ResponseSteps;
import com.epam.cucumber.steps.SharedMemory;
import com.epam.dto.trainer.TrainerProfileDto;
import com.epam.dto.trainer.TrainerProfileUpdateDto;
import com.epam.repository.TrainerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RequiredArgsConstructor
public class TrainerUpdateProfileSteps {

    private final  MockMvc mockMvc;
    private final  ObjectMapper objectMapper;
    private final  ResponseSteps responseSteps;
    private final SharedMemory sharedMemory;

    private TrainerProfileUpdateDto updateDto;


    @When("trainer updates their profile with new details")
    public void trainer_updates_profile() throws Exception {
        String username = sharedMemory.get("trainerUsername").toString();

        updateDto = TrainerProfileUpdateDto.builder()
                .username(username)
                .firstname("Azam")
                .lastname("Murodjonov")
                .specializationId(1)
                .isActive(true)
                .build();

        String json = objectMapper.writeValueAsString(updateDto);

        MvcResult result = mockMvc.perform(put("/trainers")
                        .with(user(username).roles("TRAINER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        responseSteps.setMvcResult(result);
    }

    @And("the response for trainer profile update request contains updated info")
    public void the_response_contains_updated_info() throws UnsupportedEncodingException, JsonProcessingException {
        String bodyJson = responseSteps.getMvcResult().getResponse().getContentAsString();
        TrainerProfileDto responseDto = objectMapper.readValue(bodyJson, TrainerProfileDto.class);

        assertThat(responseDto).isNotNull();
        assertEquals(updateDto.firstname(), responseDto.firstname());
        assertEquals(updateDto.lastname(), responseDto.lastname());
        assertEquals(updateDto.isActive(), responseDto.isActive());
    }
}
