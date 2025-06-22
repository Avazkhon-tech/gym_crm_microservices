package com.epam.trainerworkloadservice.service;

import com.epam.trainerworkloadservice.dto.TrainerWorkloadDto;
import com.epam.trainerworkloadservice.enums.ActionType;
import com.epam.trainerworkloadservice.exception.ResourceNotFoundException;
import com.epam.trainerworkloadservice.model.Trainer;
import com.epam.trainerworkloadservice.model.TrainerWorkload;
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
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(30)
                .build();

        TrainerWorkload existingWorkload = TrainerWorkload.builder()
                .trainerUsername(trainer.getUsername()
                )
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(60)
                .build();

        when(trainerRepository.findByUsername("john_doe")).thenReturn(Optional.of(trainer));
        when(trainerMonthlyWorkloadRepository.findByTrainerUsernameAndTrainingDate("john_doe", LocalDate.now()))
                .thenReturn(Optional.of(existingWorkload));

        // when
        trainerWorkloadService.updateTrainerWorkload(dto);

        // then
        assertEquals(90, existingWorkload.getTrainingDurationMinutes());
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
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(45)
                .build();

        Trainer newTrainer = Trainer.builder()
                .username(username)
                .firstname("Jane")
                .lastname("Smith")
                .isActive(true)
                .build();

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(trainerRepository.save(any())).thenReturn(newTrainer);
        when(trainerMonthlyWorkloadRepository.findByTrainerUsernameAndTrainingDate(username, LocalDate.now()))
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
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(20)
                .build();

        TrainerWorkload workload = TrainerWorkload.builder()
                .trainerUsername(trainer.getUsername())
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(50)
                .build();

        when(trainerRepository.findByUsername("trainer_1")).thenReturn(Optional.of(trainer));
        when(trainerMonthlyWorkloadRepository.findByTrainerUsernameAndTrainingDate("trainer_1", LocalDate.now()))
                .thenReturn(Optional.of(workload));

        // when
        trainerWorkloadService.updateTrainerWorkload(dto);

        // then
        assertEquals(30, workload.getTrainingDurationMinutes());
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
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(20)
                .build();

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainerMonthlyWorkloadRepository.findByTrainerUsernameAndTrainingDate(username, LocalDate.now()))
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
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(100)
                .build();

        TrainerWorkload workload = TrainerWorkload.builder()
                .trainerUsername(trainer.getUsername())
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(50)
                .build();

        when(trainerRepository.findByUsername("trainer_excess")).thenReturn(Optional.of(trainer));
        when(trainerMonthlyWorkloadRepository.findByTrainerUsernameAndTrainingDate("trainer_excess", LocalDate.now()))
                .thenReturn(Optional.of(workload));

        // when
        trainerWorkloadService.updateTrainerWorkload(dto);

        // then
        assertEquals(50, workload.getTrainingDurationMinutes());
        verify(trainerMonthlyWorkloadRepository).save(workload);
    }
}
