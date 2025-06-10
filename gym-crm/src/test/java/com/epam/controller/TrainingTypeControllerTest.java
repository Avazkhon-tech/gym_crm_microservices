package com.epam.controller;

import com.epam.model.TrainingType;
import com.epam.security.TestSecurityConfig;
import com.epam.service.TrainingTypeService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(TrainingTypeController.class)
@Import(TestSecurityConfig.class)
class TrainingTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainingTypeService trainingTypeService;

    @Test
    void getAllTrainingTypes() throws Exception {

        List<TrainingType> trainingTypes = Instancio.createList(TrainingType.class);

        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);

        mockMvc.perform(get("/training-types")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}