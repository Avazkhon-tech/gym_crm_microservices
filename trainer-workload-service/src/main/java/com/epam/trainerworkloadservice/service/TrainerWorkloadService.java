package com.epam.trainerworkloadservice.service;

import com.epam.trainerworkloadservice.dto.TrainerWorkloadDto;
import com.epam.trainerworkloadservice.enums.ActionType;
import com.epam.trainerworkloadservice.exception.ResourceNotFoundException;
import com.epam.trainerworkloadservice.exception.TrainerAlreadyBusyException;
import com.epam.trainerworkloadservice.model.Trainer;
import com.epam.trainerworkloadservice.model.TrainerMonthlyWorkload;
import com.epam.trainerworkloadservice.repository.TrainerMonthlyWorkloadRepository;
import com.epam.trainerworkloadservice.repository.TrainerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

    private final TrainerRepository trainerRepository;
    private final TrainerMonthlyWorkloadRepository trainerMonthlyWorkloadRepository;
    private final int MAX_ALLOWED_MINUTES = 8 * 60;

    @Transactional
    public void updateTrainerWorkload(TrainerWorkloadDto trainerWorkloadDto) {

        // find the trainer or else save it as new
        Trainer trainer = trainerRepository.findById(trainerWorkloadDto.username())
                .orElseGet(() -> trainerRepository.save(Trainer.builder()
                        .username(trainerWorkloadDto.username())
                        .firstname(trainerWorkloadDto.firstname())
                        .lastname(trainerWorkloadDto.lastname())
                        .isActive(trainerWorkloadDto.isActive())
                        .build()));

        String username = trainerWorkloadDto.username();
        int year = trainerWorkloadDto.trainingDate().getYear();
        int month = trainerWorkloadDto.trainingDate().getMonthValue();
        int duration = trainerWorkloadDto.trainingDurationMinutes();

        Optional<TrainerMonthlyWorkload> optionalWorkload = trainerMonthlyWorkloadRepository.findByTrainerUsernameAndYearAndMonth(username, year, month);

        // add the minutes
        if (trainerWorkloadDto.actionType() == ActionType.ADD) {
            TrainerMonthlyWorkload workload = optionalWorkload.orElseGet(() -> TrainerMonthlyWorkload.builder()
                    .trainer(trainer)
                    .year(year)
                    .month(month)
                    .totalDurationMinutes(0)
                    .build());

            if (workload.getTotalDurationMinutes() + duration > MAX_ALLOWED_MINUTES) {
                throw new TrainerAlreadyBusyException("Trainer exceeded daily limit");
            }

            workload.setTotalDurationMinutes(workload.getTotalDurationMinutes() + duration);
            trainerMonthlyWorkloadRepository.save(workload);
        // subtract the minutes
        } else if (trainerWorkloadDto.actionType() == ActionType.DELETE) {
            if (optionalWorkload.isPresent()) {
                TrainerMonthlyWorkload workload = optionalWorkload.get();
                if (workload.getTotalDurationMinutes() >= duration) {
                    workload.setTotalDurationMinutes(workload.getTotalDurationMinutes() - duration);
                }
                trainerMonthlyWorkloadRepository.save(workload);
            } else {
                throw ResourceNotFoundException.of("No workload found for username ", username);
            }
        }
    }
}
