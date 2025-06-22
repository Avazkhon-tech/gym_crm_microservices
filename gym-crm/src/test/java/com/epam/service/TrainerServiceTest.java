package com.epam.service;

import com.epam.dto.auth.LoginDto;
import com.epam.dto.trainer.TrainerProfileDto;
import com.epam.dto.trainer.TrainerProfileUpdateDto;
import com.epam.dto.trainer.TrainerRegistrationDto;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.mapper.TrainerMapper;
import com.epam.model.Trainer;
import com.epam.model.TrainingType;
import com.epam.model.User;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {


    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private UserService userService;

    @Spy
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TrainerService trainerService;

    private TrainerRegistrationDto trainerRegistrationRequest;
    private Trainer newTrainer;
    private Trainer savedTrainer;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {

        trainingType = new TrainingType(1, "STRENGTH_TRAINING");

        trainerRegistrationRequest =  TrainerRegistrationDto.builder()
                .specializationId(1)
                .firstname("Ahmad")
                .lastname("Valiyev")
                .build();


        newTrainer = Trainer.builder()
                .specialization(trainingType)
                .user(User.builder()
                        .firstname("Ahmad")
                        .lastname("Valiyev")
                        .username("Ahmad.Valiyev")
                        .isActive(true)
                        .password("password")
                        .build())
                .build();

        savedTrainer = Trainer.builder()
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

    }


    @Nested
    class CreateTrainerTests {

        @Test
        void shouldCreateTrainerSuccessfully() {

            when(trainerMapper.toEntity(trainerRegistrationRequest)).thenReturn(newTrainer);
            when(userService.getUniqueUsername("Ahmad", "Valiyev")).thenReturn("Ahmad.Valiyev");
            when(userService.generatePassword()).thenReturn("password");
            when(trainerRepository.save(newTrainer)).thenReturn(savedTrainer);
            when(trainingTypeRepository.findById(trainerRegistrationRequest.specializationId())).thenReturn(Optional.ofNullable(trainingType));

            LoginDto result = trainerService.createTrainer(trainerRegistrationRequest);

            assertNotNull(result);
            assertEquals("Ahmad.Valiyev", result.username());
            assertEquals("password", result.password());

            verify(trainerRepository, times(1)).save(any());
        }

        @Test
        void shouldThrowExceptionWhenTrainingTypeDoesNotExist() {

            when(trainingTypeRepository.findById(trainerRegistrationRequest.specializationId())).thenReturn(Optional.empty());

            EntityDoesNotExistException exception = assertThrows(
                    EntityDoesNotExistException.class,
                    () -> trainerService.createTrainer(trainerRegistrationRequest));

        }
    }

    @Nested
    class GetTrainerProfileByUsernameTests {

        @Test
        void shouldReturnTrainerDtoWhenTrainerExists() {
            when(trainerRepository.findByUsername("Ahmad.Valiyev")).thenReturn(Optional.of(savedTrainer));
            when(trainerMapper.toTrainerProfileDto(savedTrainer)).thenReturn(TrainerProfileDto.builder().build());

            TrainerProfileDto trainerProfileDto = trainerService.getTrainerProfileByUsername("Ahmad.Valiyev");

            assertNotNull(trainerProfileDto);

            verify(trainerRepository, times(1)).findByUsername("Ahmad.Valiyev");
            verify(trainerMapper, times(1)).toTrainerProfileDto(savedTrainer);
        }

        @Test
        void shouldThrowExceptionWhenTrainerNotFound() {
            when(trainerRepository.findByUsername("NonExistentUser")).thenReturn(Optional.empty());

            EntityDoesNotExistException exception = assertThrows(
                    EntityDoesNotExistException.class,
                    () -> trainerService.getTrainerProfileByUsername("NonExistentUser")
            );

            verify(trainerRepository, times(1)).findByUsername("NonExistentUser");
            verifyNoInteractions(trainerMapper);
        }
    }

    @Nested
    class UpdateTrainerTests {

        private TrainerProfileUpdateDto updateDto;

        @BeforeEach
        void setUp() {
            updateDto = TrainerProfileUpdateDto.builder()
                    .username(savedTrainer.getUser().getUsername())
                    .specializationId(trainingType.getId())
                    .build();
        }

        @Test
        void shouldUpdateTrainerSuccessfully() {

            when(trainerRepository.findByUsername(updateDto.username())).thenReturn(Optional.of(savedTrainer));
            when(trainerMapper.toTrainerProfileDto(savedTrainer)).thenReturn(TrainerProfileDto.builder().build());
            when(trainingTypeRepository.findById(trainingType.getId())).thenReturn(Optional.ofNullable(trainingType));


            doNothing().when(trainerMapper).updateEntity(savedTrainer, updateDto);

            TrainerProfileDto trainerProfileDto = trainerService.updateTrainer(updateDto);

            assertNotNull(trainerProfileDto);

            verify(trainerRepository, times(1)).findByUsername(updateDto.username());
            verify(trainerMapper, times(1)).updateEntity(savedTrainer, updateDto);
            verify(trainerMapper, times(1)).toTrainerProfileDto(savedTrainer);
        }

        @Test
        void shouldThrowExceptionWhenTrainerNotFound() {
            when(trainerRepository.findByUsername(updateDto.username())).thenReturn(Optional.empty());

            assertThrows(
                    EntityDoesNotExistException.class,
                    () -> trainerService.updateTrainer(updateDto));

            verify(trainerRepository, times(1)).findByUsername(updateDto.username());
            verifyNoInteractions(trainerMapper);
        }

        @Test
        void shouldThrowExceptionWhenTrainingTypeDoesNotExist() {
            when(trainerRepository.findByUsername(updateDto.username())).thenReturn(Optional.of(savedTrainer));
            when(trainingTypeRepository.findById(trainingType.getId())).thenReturn(Optional.empty());

            assertThrows(
                    EntityDoesNotExistException.class,
                    () -> trainerService.updateTrainer(updateDto));

            verify(trainingTypeRepository, times(1)).findById(trainingType.getId());
            verify(trainerRepository, times(1)).findByUsername(updateDto.username());
            verifyNoInteractions(trainerMapper);
        }
    }


    @Nested
    class UpdateActiveStatusTests {

        @Test
        void shouldUpdateActiveStatusSuccessfully() {
            when(trainerRepository.findByUsername(savedTrainer.getUser().getUsername())).thenReturn(Optional.of(savedTrainer));

            trainerService.updateActiveStatus(savedTrainer.getUser().getUsername(), false);

            boolean actual = savedTrainer.getUser().getIsActive();
            boolean expected = false;

            assertEquals(expected, actual);
        }

        @Test
        void shouldThrowExceptionWhenTrainerNotFound() {
            when(trainerRepository.findByUsername(savedTrainer.getUser().getUsername())).thenReturn(Optional.empty());

            assertThrows(EntityDoesNotExistException.class,
                    () -> trainerService.updateActiveStatus(savedTrainer.getUser().getUsername(), false));

            verify(trainerRepository, times(1)).findByUsername(savedTrainer.getUser().getUsername());
        }
    }

}