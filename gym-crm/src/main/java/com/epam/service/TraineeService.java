package com.epam.service;

import com.epam.dto.auth.LoginDto;
import com.epam.dto.trainee.TraineeProfileDto;
import com.epam.dto.trainee.TraineeProfileUpdateDto;
import com.epam.dto.trainee.TraineeRegistrationDto;
import com.epam.dto.trainee.TraineeTrainerDto;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.mapper.TraineeMapper;
import com.epam.mapper.TrainerMapper;
import com.epam.model.Trainee;
import com.epam.model.Trainer;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TraineeService {

    private final UserService userService;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;


    @Transactional
    public LoginDto createTrainee(TraineeRegistrationDto traineeRegistrationDto) {

        Trainee trainee = traineeMapper.toEntity(traineeRegistrationDto);

        trainee.getUser().setUsername(
                userService.getUniqueUsername(traineeRegistrationDto.firstname(), traineeRegistrationDto.lastname()));

        trainee.getUser().setPassword(userService.generatePassword());

        trainee = traineeRepository.save(trainee);

        return new LoginDto(trainee.getUser().getUsername(), trainee.getUser().getPassword());
    }


    public TraineeProfileDto getTraineeProfile(String username) {
        Optional<Trainee> optionalTrainee = traineeRepository.findByUsername(username);

        Trainee trainee = optionalTrainee.orElseThrow(() -> new EntityDoesNotExistException(username, "username", "Trainee"));

        return traineeMapper.toTraineeProfileDto(trainee);
    }


    @Transactional
    public TraineeProfileDto updateTrainee(String username, TraineeProfileUpdateDto traineeProfileUpdateDto) {
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new EntityDoesNotExistException(username, "username", "Trainee"));

        traineeMapper.updateEntity(trainee, traineeProfileUpdateDto);
        return traineeMapper.toTraineeProfileDto(trainee);
    }


    @Transactional
    public void deleteTrainee(String username) {
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new EntityDoesNotExistException(username, "username", "Trainee"));
        traineeRepository.delete(trainee);
    }


    public List<TraineeTrainerDto> getActiveUnassignedTrainers(String username) {

        if (!traineeRepository.existsByUsername(username))
            throw new EntityDoesNotExistException(username, "username", "Trainee");

        return traineeRepository.getActiveUnassignedTrainers(username)
                .stream()
                .map(trainerMapper::toTraineeTrainerDto)
                .toList();
    }


    @Transactional
    public List<TraineeTrainerDto> updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {

        Trainee trainee = traineeRepository.findByUsername(traineeUsername)
                .orElseThrow(() -> new EntityDoesNotExistException(traineeUsername, "username", "Trainee"));

        List<Trainer> trainers = trainerRepository.findAllByUsername(trainerUsernames);

        trainee.getTrainers().clear();
        trainee.getTrainers().addAll(trainers);

        return traineeRepository.save(trainee).getTrainers().stream()
                .map(trainerMapper::toTraineeTrainerDto)
                .toList();
    }

    @Transactional
    public void updateActiveStatus(String username, Boolean isActive) {
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new EntityDoesNotExistException(username, "username", "Trainee"));
        trainee.getUser().setIsActive(isActive);
    }
}
