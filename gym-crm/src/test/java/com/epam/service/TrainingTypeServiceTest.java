package com.epam.service;

import com.epam.model.TrainingType;
import com.epam.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {


    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    private List<TrainingType> trainingTypes;

    @BeforeEach
    void setUp() {
        trainingTypes = List.of(
                new TrainingType(1, "Training Type 1"),
                new TrainingType(2, "Training Type 2"),
                new TrainingType(3, "Training Type 3")
        );
    }


    @Test
    void getAllTrainingTypesWhenNotEmpty() {
        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        List<TrainingType> allTrainingTypes = trainingTypeService.getAllTrainingTypes();

        assertNotNull(allTrainingTypes);
        assertEquals(allTrainingTypes, trainingTypes);
        verify(trainingTypeRepository).findAll();
    }

}