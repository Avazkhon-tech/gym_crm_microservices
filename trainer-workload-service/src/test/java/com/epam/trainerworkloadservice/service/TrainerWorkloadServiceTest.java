package com.epam.trainerworkloadservice.service;

import com.epam.trainerworkloadservice.dto.TrainerWorkloadDto;
import com.epam.trainerworkloadservice.enums.ActionType;
import com.epam.trainerworkloadservice.exception.ResourceNotFoundException;
import com.epam.trainerworkloadservice.model.Trainer;
import com.epam.trainerworkloadservice.model.TrainerMonthlyWorkload;
import com.epam.trainerworkloadservice.repository.TrainerMonthlyWorkloadRepository;
import com.epam.trainerworkloadservice.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceTest {

    @Mock
    TrainerRepository trainerRepository;

    @Mock
    TrainerMonthlyWorkloadRepository trainerMonthlyWorkloadRepository;

    @InjectMocks
    TrainerWorkloadService trainerWorkloadService;

    @Test
    void shouldUpdateTrainerWorkloadSuccessfully_whenTrainerAndWorkloadExist() {
        // given
        Trainer trainer = Trainer.builder()
                .username("john_doe")
                .firstname("John")
                .lastname("Doe")
                .isActive(true)
                .build();

        TrainerWorkloadDto dto = TrainerWorkloadDto.builder()
                .actionType(ActionType.ADD)
                .username("john_doe")
                .trainingDate(LocalDate.of(2024, 5, 1))
                .trainingDurationMinutes(30)
                .build();

        TrainerMonthlyWorkload existingWorkload = TrainerMonthlyWorkload.builder()
                .trainer(trainer)
                .year(2024)
                .month(5)
                .totalDurationMinutes(60)
                .build();

        when(trainerRepository.findById("john_doe")).thenReturn(Optional.of(trainer));
        when(trainerMonthlyWorkloadRepository.findByTrainerUsernameAndYearAndMonth("john_doe", 2024, 5))
                .thenReturn(Optional.of(existingWorkload));

        // when
        trainerWorkloadService.updateTrainerWorkload(dto);

        // then
        assertEquals(90, existingWorkload.getTotalDurationMinutes());
        verify(trainerMonthlyWorkloadRepository).save(existingWorkload);
    }

    @Test
    void shouldCreateTrainerAndNewWorkload_whenTrainerDoesNotExist() {
        // given
        String username = "new_trainer";
        TrainerWorkloadDto dto = TrainerWorkloadDto.builder()
                .actionType(ActionType.ADD)
                .username(username)
                .firstname("Jane")
                .lastname("Smith")
                .isActive(true)
                .trainingDate(LocalDate.of(2024, 5, 1))
                .trainingDurationMinutes(45)
                .build();

        Trainer newTrainer = Trainer.builder()
                .username(username)
                .firstname("Jane")
                .lastname("Smith")
                .isActive(true)
                .build();

        when(trainerRepository.findById(username)).thenReturn(Optional.empty());
        when(trainerRepository.save(any())).thenReturn(newTrainer);
        when(trainerMonthlyWorkloadRepository.findByTrainerUsernameAndYearAndMonth(username, 2024, 5))
                .thenReturn(Optional.empty());

        // when
        trainerWorkloadService.updateTrainerWorkload(dto);

        // then
        verify(trainerRepository).save(any());
        verify(trainerMonthlyWorkloadRepository).save(any());
    }

    @Test
    void shouldSubtractDuration_whenWorkloadIsSufficient() {
        // given
        Trainer trainer = Trainer.builder()
                .username("trainer_1")
                .firstname("Alice")
                .lastname("Brown")
                .isActive(true)
                .build();

        TrainerWorkloadDto dto = TrainerWorkloadDto.builder()
                .actionType(ActionType.DELETE)
                .username("trainer_1")
                .trainingDate(LocalDate.of(2024, 6, 1))
                .trainingDurationMinutes(20)
                .build();

        TrainerMonthlyWorkload workload = TrainerMonthlyWorkload.builder()
                .trainer(trainer)
                .year(2024)
                .month(6)
                .totalDurationMinutes(50)
                .build();

        when(trainerRepository.findById("trainer_1")).thenReturn(Optional.of(trainer));
        when(trainerMonthlyWorkloadRepository.findByTrainerUsernameAndYearAndMonth("trainer_1", 2024, 6))
                .thenReturn(Optional.of(workload));

        // when
        trainerWorkloadService.updateTrainerWorkload(dto);

        // then
        assertEquals(30, workload.getTotalDurationMinutes());
        verify(trainerMonthlyWorkloadRepository).save(workload);
    }

    @Test
    void shouldThrowException_whenWorkloadNotFoundForDelete() {
        // given
        String username = "unknown_trainer";
        Trainer trainer = Trainer.builder()
                .username(username)
                .firstname("Unknown")
                .lastname("Trainer")
                .isActive(true)
                .build();

        TrainerWorkloadDto dto = TrainerWorkloadDto.builder()
                .actionType(ActionType.DELETE)
                .username(username)
                .trainingDate(LocalDate.of(2024, 7, 1))
                .trainingDurationMinutes(20)
                .build();

        when(trainerRepository.findById(username)).thenReturn(Optional.of(trainer));
        when(trainerMonthlyWorkloadRepository.findByTrainerUsernameAndYearAndMonth(username, 2024, 7))
                .thenReturn(Optional.empty());

        // expect
        assertThrows(ResourceNotFoundException.class, () -> trainerWorkloadService.updateTrainerWorkload(dto));
    }

    @Test
    void shouldNotSubtractIfDurationExceedsWorkload() {
        // given
        Trainer trainer = Trainer.builder()
                .username("trainer_excess")
                .firstname("Max")
                .lastname("Load")
                .isActive(true)
                .build();

        TrainerWorkloadDto dto = TrainerWorkloadDto.builder()
                .actionType(ActionType.DELETE)
                .username("trainer_excess")
                .trainingDate(LocalDate.of(2024, 8, 1))
                .trainingDurationMinutes(100)
                .build();

        TrainerMonthlyWorkload workload = TrainerMonthlyWorkload.builder()
                .trainer(trainer)
                .year(2024)
                .month(8)
                .totalDurationMinutes(50)
                .build();

        when(trainerRepository.findById("trainer_excess")).thenReturn(Optional.of(trainer));
        when(trainerMonthlyWorkloadRepository.findByTrainerUsernameAndYearAndMonth("trainer_excess", 2024, 8))
                .thenReturn(Optional.of(workload));

        // when
        trainerWorkloadService.updateTrainerWorkload(dto);

        // then
        assertEquals(50, workload.getTotalDurationMinutes()); // unchanged
        verify(trainerMonthlyWorkloadRepository).save(workload);
    }
}
