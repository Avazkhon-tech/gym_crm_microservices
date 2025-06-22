package com.epam.trainerworkloadservice.repository;

import com.epam.trainerworkloadservice.model.TrainerWorkload;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TrainerMonthlyWorkloadRepository extends MongoRepository<TrainerWorkload, Long> {

    Optional<TrainerWorkload> findByTrainerUsernameAndTrainingDate(String username, LocalDate trainingDate);
}
