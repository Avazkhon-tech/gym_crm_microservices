package com.epam.service;

import com.epam.dto.auth.LoginDto;
import com.epam.dto.trainer.TrainerProfileDto;
import com.epam.dto.trainer.TrainerProfileUpdateDto;
import com.epam.dto.trainer.TrainerRegistrationDto;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.mapper.TrainerMapper;
import com.epam.model.Trainer;
import com.epam.model.TrainingType;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingTypeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final UserService userService;
    private final TrainerMapper trainerMapper;

    @Transactional
    public LoginDto createTrainer(TrainerRegistrationDto trainerRegistrationRequest) {

        TrainingType specialization = trainingTypeRepository.findById(trainerRegistrationRequest.specializationId())
                .orElseThrow(() -> new EntityDoesNotExistException(trainerRegistrationRequest.specializationId(), "ID" , "Specialization"));

        Trainer trainer = trainerMapper.toEntity(trainerRegistrationRequest);

        trainer.setSpecialization(specialization);

        trainer.getUser().setUsername(
                userService.getUniqueUsername(trainerRegistrationRequest.firstname(), trainerRegistrationRequest.lastname()));

        trainer.getUser().setPassword(userService.generatePassword());

        Trainer savedTrainer = trainerRepository.save(trainer);
        return new LoginDto(savedTrainer.getUser().getUsername(), savedTrainer.getUser().getPassword());
    }


    public TrainerProfileDto getTrainerProfileByUsername(String username) {
        Optional<Trainer> trainerOptional = trainerRepository.findByUsername(username);
        Trainer trainer = trainerOptional
                .orElseThrow(() -> new EntityDoesNotExistException(username, "username", "Trainer"));
        return trainerMapper.toTrainerProfileDto(trainer);
    }


    @Transactional
    public TrainerProfileDto updateTrainer(TrainerProfileUpdateDto trainerProfileUpdateDto) {
        Trainer trainer = trainerRepository.findByUsername(trainerProfileUpdateDto.username())
                .orElseThrow(() -> new EntityDoesNotExistException(trainerProfileUpdateDto.username(), "username", "Trainer"));

        TrainingType trainingType = trainingTypeRepository.findById(trainerProfileUpdateDto.specializationId())
                .orElseThrow(() -> new EntityDoesNotExistException(trainerProfileUpdateDto.specializationId(), "ID", "Specialization"));

        trainerMapper.updateEntity(trainer, trainerProfileUpdateDto);

        trainer.setSpecialization(trainingType);

        return trainerMapper.toTrainerProfileDto(trainer);
    }


    @Transactional
    public void updateActiveStatus(String username, Boolean isActive) {
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityDoesNotExistException(username, "username", "Trainer"));
        trainer.getUser().setIsActive(isActive);
    }
}
