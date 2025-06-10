package com.epam.trainerworkloadservice.repository;

import com.epam.trainerworkloadservice.model.TrainerMonthlyWorkload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerMonthlyWorkloadRepository extends JpaRepository<TrainerMonthlyWorkload, Long> {

    Optional<TrainerMonthlyWorkload> findByTrainerUsernameAndYearAndMonth(String username, int year, int month);
}
