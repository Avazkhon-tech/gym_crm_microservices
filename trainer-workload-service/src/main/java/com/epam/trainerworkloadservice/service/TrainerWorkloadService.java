package com.epam.trainerworkloadservice.service;

import com.epam.trainerworkloadservice.dto.TrainerWorkloadDto;
import com.epam.trainerworkloadservice.enums.ActionType;
import com.epam.trainerworkloadservice.exception.ResourceNotFoundException;
import com.epam.trainerworkloadservice.exception.TrainerAlreadyBusyException;
import com.epam.trainerworkloadservice.model.Trainer;
import com.epam.trainerworkloadservice.model.TrainerWorkload;
import com.epam.trainerworkloadservice.repository.TrainerMonthlyWorkloadRepository;
import com.epam.trainerworkloadservice.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

    private final TrainerRepository trainerRepository;
    private final TrainerMonthlyWorkloadRepository trainerMonthlyWorkloadRepository;
    private static final int MAX_ALLOWED_MINUTES = 8 * 60;


    public void updateTrainerWorkload(TrainerWorkloadDto trainerWorkloadDto) {

        // find the trainer or else save it as new
        Trainer trainer = trainerRepository.findByUsername(trainerWorkloadDto.username())
                .orElseGet(() -> trainerRepository.save(Trainer.builder()
                        .username(trainerWorkloadDto.username())
                        .firstname(trainerWorkloadDto.firstname())
                        .lastname(trainerWorkloadDto.lastname())
                        .isActive(trainerWorkloadDto.isActive())
                        .build()));

        String username = trainerWorkloadDto.username();
        LocalDate trainingDate = trainerWorkloadDto.trainingDate();
        int duration = trainerWorkloadDto.trainingDurationMinutes();

        Optional<TrainerWorkload> optionalWorkload = trainerMonthlyWorkloadRepository.findByTrainerUsernameAndTrainingDate(username, trainingDate);

        // add the minutes
        if (trainerWorkloadDto.actionType() == ActionType.ADD) {
            TrainerWorkload workload = optionalWorkload.orElseGet(() -> TrainerWorkload.builder()
                    .trainerUsername(trainer.getUsername())
                    .trainingDate(trainingDate)
                    .trainingDurationMinutes(0)
                    .build());

            if (workload.getTrainingDurationMinutes() + duration > MAX_ALLOWED_MINUTES) {
                throw new TrainerAlreadyBusyException("Trainer exceeded daily limit");
            }

            workload.setTrainingDurationMinutes(workload.getTrainingDurationMinutes() + duration);
            trainerMonthlyWorkloadRepository.save(workload);
        // subtract the minutes
        } else if (trainerWorkloadDto.actionType() == ActionType.DELETE) {
            if (optionalWorkload.isPresent()) {
                TrainerWorkload workload = optionalWorkload.get();
                if (workload.getTrainingDurationMinutes() >= duration) {
                    workload.setTrainingDurationMinutes(workload.getTrainingDurationMinutes() - duration);
                }
                trainerMonthlyWorkloadRepository.save(workload);
            } else {
                throw ResourceNotFoundException.of("No workload found for username ", username);
            }
        }
    }
}
