package com.epam.service;

import com.epam.model.TrainingType;
import com.epam.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeRepository.findAll();
    }
}
