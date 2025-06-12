package com.epam.service;

import com.epam.dto.trainee.TraineeTrainingFilter;
import com.epam.dto.trainer.TrainerTrainingFilter;
import com.epam.dto.tranining.TraineeTrainingDto;
import com.epam.dto.tranining.TrainerTrainingDto;
import com.epam.dto.tranining.TrainingCreateDto;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.feign.TrainerWorkloadClient;
import com.epam.mapper.TrainingMapper;
import com.epam.model.*;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingRepository;
import com.epam.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private TrainerWorkloadClient trainerWorkloadClient;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Spy
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;

    private Trainer trainer;

    private Trainee trainee;

    private TrainingType trainingType;

    @BeforeEach
    void setUp() {

        trainingType = new TrainingType(1, "STRENGTH_TRAINING");

        trainer = Trainer.builder()
                .id(1L)
                .specialization(trainingType)
                .user(User.builder()
                        .firstname("Ahmad")
                        .lastname("Valiyev")
                        .username("Ahmad.Valiyev")
                        .isActive(true)
                        .password("password")
                        .build())
                .build();

        trainee = Trainee.builder()
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


        training = Training.builder()
                .id(1L)
                .trainee(trainee)
                .trainer(trainer)
                .trainingName("Strength & Conditioning")
                .build();

    }

    @Nested
    class GetTrainingTests {


        @Test
        void shouldReturnListOfTrainingsSuccessfully() {

            when(trainingRepository.findAll()).thenReturn(List.of(training));

            List<TrainerTrainingDto> allTraining = trainingService.getAllTraining();

            assertNotNull(allTraining);

            assertFalse(allTraining.isEmpty());
        }

        @Test
        void shouldReturnEmptyListWhenTrainingsDoNotExist() {
            when(trainingRepository.findAll()).thenReturn(List.of());

            List<TrainerTrainingDto> allTraining = trainingService.getAllTraining();

            assertNotNull(allTraining);

            assertTrue(allTraining.isEmpty());
        }
    }


    @Nested
    class AddTrainingTests {
        
        private TrainingCreateDto trainingCreateDto;
        
        @BeforeEach
        void setUp() {
            trainingCreateDto =  TrainingCreateDto.builder()
                    .trainingName(training.getTrainingName())
                    .traineeUsername(trainee.getUser().getUsername())
                    .trainerUsername(trainer.getUser().getUsername())
                    .build();
        }

        @Test
        void shouldAddTrainingSuccessfully() {
            // Mocks for trainer and trainee
            when(trainerRepository.findByUsername(trainer.getUser().getUsername())).thenReturn(Optional.of(trainer));
            when(traineeRepository.findByUsername(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
            when(trainingMapper.toEntity(trainingCreateDto)).thenReturn(new Training());

            // Mocking SecurityContext and Authentication
            Authentication authentication = mock(Authentication.class);
            when(authentication.getName()).thenReturn(trainee.getUser().getUsername());

            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
                mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

                // Act
                trainingService.createTraining(trainingCreateDto);

                // Assert
                verify(traineeRepository).findByUsername(trainee.getUser().getUsername());
                verify(trainerRepository).findByUsername(trainer.getUser().getUsername());
            }
        }

        @Test
        void shouldThrowExceptionWhenTraineeNotFound() {
            when(traineeRepository.findByUsername(trainingCreateDto.traineeUsername())).thenReturn(Optional.empty());
            assertThrows(EntityDoesNotExistException.class, () -> trainingService.createTraining(trainingCreateDto));
        }

        @Test
        void shouldThrowExceptionWhenTrainerNotFound() {
            when(traineeRepository.findByUsername(trainingCreateDto.traineeUsername())).thenReturn(Optional.of(trainee));
            when(trainerRepository.findByUsername(trainingCreateDto.trainerUsername())).thenReturn(Optional.empty());
            assertThrows(EntityDoesNotExistException.class, () -> trainingService.createTraining(trainingCreateDto));
        }
    }

    @Nested
    class GetTraineeTrainingsByCriteriaTests {

        private TraineeTrainingFilter criteria;
        private List<Training> expectedTrainings;
        @BeforeEach
        void setUp() {
           criteria = new TraineeTrainingFilter(
                    trainee.getUser().getUsername(),
                    LocalDate.of(2023, 1, 1),
                    LocalDate.of(2023, 12, 31),
                    "John Doe",
                    "Strength Training"
            );

            expectedTrainings = List.of(
                    Training.builder()
                            .id(1L)
                            .trainee(trainee)
                            .trainer(trainer)
                            .trainingName("Strength & Conditioning")
                            .trainingType(trainingType)
                            .trainingDate(LocalDate.of(2023, 5, 10))
                            .trainingDurationMinutes(60)
                            .build(),
                    Training.builder()
                            .id(2L)
                            .trainee(trainee)
                            .trainer(trainer)
                            .trainingName("Endurance Training")
                            .trainingType(trainingType)
                            .trainingDate(LocalDate.of(2023, 6, 15))
                            .trainingDurationMinutes(90)
                            .build()
            );
        }

        @Test
        void shouldReturnTrainingsWhenCriteriaMatches() {
            when(traineeRepository.existsByUsername(trainer.getUser().getUsername())).thenReturn(true);
            when(trainingRepository.getTraineeTrainingsByCriteria(criteria)).thenReturn(expectedTrainings);

            List<TraineeTrainingDto> actualTrainings = trainingService.getTraineeTrainingsByCriteria(criteria);

            assertEquals(expectedTrainings.size(), actualTrainings.size());
            verify(trainingRepository).getTraineeTrainingsByCriteria(criteria);
        }

        @Test
        void shouldThrowExceptionWhenTraineeNotFound() {
            when(traineeRepository.existsByUsername(trainer.getUser().getUsername())).thenReturn(false);
            assertThrows(EntityDoesNotExistException.class, () -> trainingService.getTraineeTrainingsByCriteria(criteria));
        }
    }



    @Nested
    class GetTrainerTrainingsByCriteriaTests {

        private TrainerTrainingFilter criteria;
        private List<Training> expectedTrainings;

        @BeforeEach
        void setUp() {
            criteria = new TrainerTrainingFilter(
                    trainer.getUser().getUsername(),
                    LocalDate.of(2023, 1, 1),
                    LocalDate.of(2023, 12, 31),
                    "Jane Smith"
            );

            expectedTrainings = List.of(
                    Training.builder()
                            .id(1L)
                            .trainee(trainee)
                            .trainer(trainer)
                            .trainingName("Morning Fitness")
                            .trainingType(trainingType)
                            .trainingDate(LocalDate.of(2023, 5, 10))
                            .trainingDurationMinutes(60)
                            .build(),
                    Training.builder()
                            .id(2L)
                            .trainee(trainee)
                            .trainer(trainer)
                            .trainingName("Evening Yoga")
                            .trainingType(trainingType)
                            .trainingDate(LocalDate.of(2023, 6, 15))
                            .trainingDurationMinutes(90)
                            .build()
            );
        }

        @Test
        void shouldReturnTrainingsWhenCriteriaMatches() {
            when(trainerRepository.existsByUsername(trainer.getUser().getUsername())).thenReturn(true);
            when(trainingRepository.getTrainerTrainingsByCriteria(criteria)).thenReturn(expectedTrainings);

            List<TrainerTrainingDto> actualTrainings = trainingService.getTrainerTrainingsByCriteria(criteria);

            assertEquals(expectedTrainings.size(), actualTrainings.size());
            Mockito.verify(trainingRepository).getTrainerTrainingsByCriteria(criteria);
        }

        @Test
        void shouldThrowExceptionWhenTrainerNotFound() {
            when(trainerRepository.existsByUsername(trainer.getUser().getUsername())).thenReturn(false);
            assertThrows(EntityDoesNotExistException.class, () -> trainingService.getTrainerTrainingsByCriteria(criteria));
        }
    }

}