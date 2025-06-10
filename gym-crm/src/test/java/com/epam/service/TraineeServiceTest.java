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
import com.epam.model.User;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserService userService;

    @Spy
    private TraineeMapper traineeMapper;

    @Spy
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TraineeService traineeService;

    private TraineeProfileDto traineeProfileDto;
    private TraineeRegistrationDto traineeRegistrationDto;
    private Trainee newTrainee;
    private Trainee savedTrainee;

    @BeforeEach
    void setUp() {

        traineeProfileDto = TraineeProfileDto.builder()
                .dateOfBirth(LocalDate.now().minusYears(20))
                .address("Tashkent")
                .firstname("Ahmad")
                .lastname("Valiyev")
                .build();

        traineeRegistrationDto = TraineeRegistrationDto.builder()
                .dateOfBirth(LocalDate.now().minusYears(20))
                .address("Tashkent")
                .firstname("Ahmad")
                .lastname("Valiyev")
                .build();

        newTrainee = Trainee.builder()
                .dateOfBirth(LocalDate.now().minusYears(20))
                .address("Tashkent")
                .user(User.builder()
                        .firstname("Ahmad")
                        .lastname("Valiyev")
                        .username("Ahmad.Valiyev")
                        .isActive(true)
                        .password("password")
                        .build())
                .build();

        savedTrainee = Trainee.builder()
                .id(1L)
                .dateOfBirth(LocalDate.now().minusYears(20))
                .address("Tashkent")
                .user(User.builder()
                        .firstname("Ahmad")
                        .lastname("Valiyev")
                        .username("Ahmad.Valiyev")
                        .isActive(true)
                        .password("password")
                        .build())
                .build();

    }

    @Nested
    class CreateTraineeTests {

        @Test
        void shouldCreateTraineeSuccessfully() {

            when(traineeMapper.toEntity(traineeRegistrationDto)).thenReturn(newTrainee);
            when(userService.getUniqueUsername("Ahmad", "Valiyev")).thenReturn("Ahmad.Valiyev");
            when(userService.generatePassword()).thenReturn("password");
            when(traineeRepository.save(newTrainee)).thenReturn(savedTrainee);

            LoginDto result = traineeService.createTrainee(traineeRegistrationDto);

            assertNotNull(result);
            assertEquals("Ahmad.Valiyev", result.username());
            assertEquals("password", result.password());

            verify(traineeRepository).save(newTrainee);
            verify(traineeRepository, times(1)).save(any());
        }
    }

    @Nested
    class GetTraineeByUsernameTests {

        @Test
        void shouldReturnTraineeProfileDtoWhenTraineeExists() {
            String username = "Ahmad.Valiyev";
            when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(savedTrainee));
            when(traineeMapper.toTraineeProfileDto(savedTrainee)).thenReturn(traineeProfileDto);

            TraineeProfileDto traineeProfileByUsername = traineeService.getTraineeProfile(username);

            assertNotNull(traineeProfileByUsername);
            assertEquals(traineeProfileDto, traineeProfileByUsername);

            verify(traineeRepository).findByUsername(username);
            verify(traineeMapper).toTraineeProfileDto(savedTrainee);
        }
        
        @Test
        void shouldThrowExceptionWhenTraineeNotFound() {
            String username = "NonexistentUsername";
            when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

            assertThrows(
                    EntityDoesNotExistException.class,
                    () -> traineeService.getTraineeProfile(username));

            verify(traineeRepository).findByUsername(username);
            verify(traineeMapper, never()).toTraineeProfileDto(any());
        }
    }

    @Nested
    class UpdateTraineeTests {

        @Test
        void shouldUpdateTraineeSuccessfully() {
            when(traineeRepository.findByUsername(savedTrainee.getUser().getUsername())).thenReturn(Optional.of(savedTrainee));

            TraineeProfileUpdateDto profileUpdateDto = TraineeProfileUpdateDto.builder()
                    .address("Samarkand")
                    .firstname("Ali")
                    .lastname("Vali")
                    .build();

            traineeService.updateTrainee(savedTrainee.getUser().getUsername(), profileUpdateDto);
            verify(traineeMapper).updateEntity(savedTrainee, profileUpdateDto);
        }

        @Test
        void shouldThrowExceptionWhenTraineeNotFound() {
            String username = "NonexistentUsername";
            when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

            assertThrows(EntityDoesNotExistException.class,
                    () -> traineeService.updateTrainee(username, TraineeProfileUpdateDto.builder().build()));
        }
    }

    @Nested
    class DeleteTraineeTests {

        @Test
        void shouldDeleteTraineeByIdSuccessfully() {
            String username = savedTrainee.getUser().getUsername();
            when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(savedTrainee));

            traineeService.deleteTrainee(username);

            verify(traineeRepository).findByUsername(username);
            verify(traineeRepository).delete(savedTrainee);
        }

        @Test
        void shouldThrowExceptionWhenTraineeIdNotFound() {
            String username = "Ahmad.Valiyev";
            when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

            assertThrows(EntityDoesNotExistException.class, () -> traineeService.deleteTrainee(username));

            verify(traineeRepository).findByUsername(username);
            verify(traineeRepository, never()).deleteById(any());
        }

        @Test
        void shouldDeleteTraineeByUsernameSuccessfully() {
            String username = "Ahmad.Valiyev";
            when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(savedTrainee));

            traineeService.deleteTrainee(username);

            verify(traineeRepository).findByUsername(username);
            verify(traineeRepository).delete(savedTrainee);
        }

        @Test
        void shouldThrowExceptionWhenTraineeUsernameNotFound() {
            String username = "NonexistentUser";
            when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

            assertThrows(EntityDoesNotExistException.class, () -> traineeService.deleteTrainee(username));

            verify(traineeRepository).findByUsername(username);
            verify(traineeRepository, never()).delete(any());
        }
    }


    @Nested
    class GetActiveUnassignedTrainersTests {

        @Test
        void shouldReturnUnassignedTrainersSuccessfully() {
            String username = "Ahmad.Valiyev";
            List<Trainer> expectedTrainers = List.of(new Trainer(), new Trainer());

            when(traineeRepository.existsByUsername(username)).thenReturn(true);
            when(traineeRepository.getActiveUnassignedTrainers(username)).thenReturn(expectedTrainers);

            List<TraineeTrainerDto> result = traineeService.getActiveUnassignedTrainers(username);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(expectedTrainers.size(), result.size());

            verify(traineeRepository).getActiveUnassignedTrainers(username);
        }

        @Test
        void shouldReturnEmptyListWhenNoUnassignedTrainersFound() {
            String username = "Ahmad.Valiyev";
            when(traineeRepository.getActiveUnassignedTrainers(username)).thenReturn(Collections.emptyList());
            when(traineeRepository.existsByUsername(username)).thenReturn(true);

            List<TraineeTrainerDto> result = traineeService.getActiveUnassignedTrainers(username);

            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(traineeRepository).getActiveUnassignedTrainers(username);
        }

        @Test
        void shouldThrowExceptionWhenTraineeDoesNotExists() {
            String username = "NonexistentUsername";
            when(traineeRepository.existsByUsername(username)).thenReturn(false);

            assertThrows(EntityDoesNotExistException.class, () -> traineeService.getActiveUnassignedTrainers(username));

        }
    }

    @Nested
    class UpdateTraineeTrainersTests {

        @Test
        void shouldUpdateTraineeTrainersSuccessfully() {
            String traineeUsername = "Ahmad.Valiyev";
            List<String> trainerUsernames = List.of("Trainer1", "Trainer2");

            Trainee trainee = new Trainee();
            trainee.setTrainers(new ArrayList<>());
            List<Trainer> trainers = List.of(Trainer.builder().build(), Trainer.builder().build());
            List<TraineeTrainerDto> expectedTrainerDtos = List.of(TraineeTrainerDto.builder().build(), TraineeTrainerDto.builder().build());

            when(traineeRepository.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
            when(trainerRepository.findAllByUsernames(trainerUsernames)).thenReturn(trainers);
            when(trainerMapper.toTraineeTrainerDto(any(Trainer.class))).thenReturn(TraineeTrainerDto.builder().build());
            when(traineeRepository.save(trainee)).thenReturn(trainee);

            List<TraineeTrainerDto> result = traineeService.updateTraineeTrainers(traineeUsername, trainerUsernames);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(expectedTrainerDtos.size(), result.size());

            verify(traineeRepository).findByUsername(traineeUsername);
            verify(trainerRepository).findAllByUsernames(trainerUsernames);
            verify(traineeRepository).save(trainee);
        }

        @Test
        void shouldThrowExceptionWhenTraineeNotFound() {
            String traineeUsername = "NonexistentTrainee";
            List<String> trainerUsernames = List.of("Trainer1", "Trainer2");

            when(traineeRepository.findByUsername(traineeUsername)).thenReturn(Optional.empty());

            assertThrows(
                    EntityDoesNotExistException.class,
                    () -> traineeService.updateTraineeTrainers(traineeUsername, trainerUsernames)
            );

            verify(traineeRepository).findByUsername(traineeUsername);
            verify(trainerRepository, never()).findAllByUsernames(any());
            verify(traineeRepository, never()).save(any());
        }
    }


    @Nested
    class UpdateActiveStatusTests {

        @Test
        void shouldUpdateActiveStatusSuccessfully() {

            when(traineeRepository.findByUsername(savedTrainee.getUser().getUsername())).thenReturn(Optional.of(savedTrainee));

            traineeService.updateActiveStatus(savedTrainee.getUser().getUsername(), false);

            boolean actual = savedTrainee.getUser().getIsActive();
            boolean expected = false;

            assertEquals(actual, expected);
        }

        @Test
        void shouldThrowExceptionWhenTraineeNotFound() {
            String username = "NonexistentTrainee";

            when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

            assertThrows(EntityDoesNotExistException.class,
                    () -> traineeService.updateActiveStatus(username, false));
        }
    }

}
