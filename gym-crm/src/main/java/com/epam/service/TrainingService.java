package com.epam.service;

import com.epam.dto.trainee.TraineeTrainingFilter;
import com.epam.dto.trainer.TrainerTrainingFilter;
import com.epam.dto.training.TraineeTrainingDto;
import com.epam.dto.training.TrainerTrainingDto;
import com.epam.dto.training.TrainerWorkloadDto;
import com.epam.dto.training.TrainingCreateDto;
import com.epam.enums.ActionType;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.mapper.TrainingMapper;
import com.epam.messaging.MessageSender;
import com.epam.model.Trainee;
import com.epam.model.Trainer;
import com.epam.model.Training;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingRepository;
import com.epam.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;
    private final JwtProvider jwtProvider;
    private final MessageSender messageSender;


    public List<TrainerTrainingDto> getAllTraining() {
        return trainingRepository.findAll()
                .stream()
                .map(trainingMapper::toTrainerTrainingDto)
                .toList();
    }


    public List<TraineeTrainingDto> getTraineeTrainingsByCriteria(TraineeTrainingFilter criteria) {
        if (!traineeRepository.existsByUsername(criteria.username()))
            throw new EntityDoesNotExistException(criteria.username(), "username", "Trainee");

        return trainingRepository.getTraineeTrainingsByCriteria(criteria)
                .stream().map(trainingMapper::toTraineeTrainingDto).toList();
    }


    public List<TrainerTrainingDto> getTrainerTrainingsByCriteria(TrainerTrainingFilter criteria) {
        if (!trainerRepository.existsByUsername(criteria.username()))
            throw new EntityDoesNotExistException(criteria.username(), "username", "Trainer");

        return trainingRepository.getTrainerTrainingsByCriteria(criteria)
                .stream()
                .map(trainingMapper::toTrainerTrainingDto)
                .toList();
    }


    @Transactional
    public void createTraining(TrainingCreateDto trainingCreateDtoDto) {
        Trainee trainee = traineeRepository.findByUsername(trainingCreateDtoDto.traineeUsername())
                .orElseThrow(() -> new EntityDoesNotExistException(trainingCreateDtoDto.traineeUsername(), "username", "Trainee"));

        Trainer trainer = trainerRepository.findByUsername(trainingCreateDtoDto.trainerUsername())
                .orElseThrow(() -> new EntityDoesNotExistException(trainingCreateDtoDto.trainerUsername(), "username", "Trainer"));

        Training entity = trainingMapper.toEntity(trainingCreateDtoDto);

        entity.setTrainee(trainee);
        entity.setTrainer(trainer);
        entity.setTrainingType(trainer.getSpecialization());
        trainingRepository.save(entity);

        TrainerWorkloadDto trainerWorkloadDto = TrainerWorkloadDto.builder()
                .username(trainingCreateDtoDto.trainerUsername())
                .firstname(trainer.getUser().getFirstname())
                .lastname(trainer.getUser().getLastname())
                .isActive(trainer.getUser().getIsActive())
                .trainingDate(trainingCreateDtoDto.trainingDate())
                .trainingDurationMinutes(trainingCreateDtoDto.trainingDurationMinutes())
                .actionType(ActionType.ADD)
                .build();

        messageSender.sendTrainerWorkload(trainerWorkloadDto);

    }
}

