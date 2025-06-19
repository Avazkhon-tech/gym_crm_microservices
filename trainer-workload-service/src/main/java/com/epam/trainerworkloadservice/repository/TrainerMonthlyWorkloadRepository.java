package com.epam.trainerworkloadservice.repository;

import com.epam.trainerworkloadservice.model.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TrainerMonthlyWorkloadRepository extends JpaRepository<TrainerWorkload, Long> {

    Optional<TrainerWorkload> findByTrainerUsernameAndTrainingDate(String username, LocalDate trainingDate);
}
