package com.epam.repository;

import com.epam.dto.trainee.TraineeTrainingFilter;
import com.epam.dto.trainer.TrainerTrainingFilter;
import com.epam.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("""
            SELECT t FROM Training t
            JOIN FETCH t.trainingType
            JOIN FETCH t.trainer trainer
            JOIN FETCH trainer.user trainerUser
            JOIN FETCH trainer.specialization
            JOIN FETCH t.trainee trainee
            JOIN FETCH trainee.user traineeUser
            WHERE (:#{#searchCriteria.traineeName} IS NULL OR LOWER(traineeUser.firstname) LIKE LOWER(CONCAT('%', :#{#searchCriteria.traineeName}, '%')))
            AND (:#{#searchCriteria.fromDate} IS NULL OR t.trainingDate >= :#{#searchCriteria.fromDate})
            AND (:#{#searchCriteria.toDate} IS NULL OR t.trainingDate <= :#{#searchCriteria.toDate})
            AND trainerUser.username = :#{#searchCriteria.username}
            """)
    List<Training> getTrainerTrainingsByCriteria(@Param("searchCriteria") TrainerTrainingFilter searchCriteria);

    @Query("""
            SELECT t FROM Training t
            JOIN FETCH t.trainingType
            JOIN FETCH t.trainer trainer
            JOIN FETCH trainer.user trainerUser
            JOIN FETCH trainer.specialization
            JOIN FETCH t.trainee trainee
            JOIN FETCH trainee.user traineeUser
            WHERE (:#{#searchCriteria.trainerName} IS NULL OR LOWER(trainerUser.firstname) LIKE LOWER(CONCAT('%', :#{#searchCriteria.trainerName}, '%')))
            AND (:#{#searchCriteria.fromDate} IS NULL OR t.trainingDate >= :#{#searchCriteria.fromDate})
            AND (:#{#searchCriteria.toDate} IS NULL OR t.trainingDate <= :#{#searchCriteria.toDate})
            AND (:#{#searchCriteria.trainingType} IS NULL OR t.trainingType.name = :#{#searchCriteria.trainingType})
            AND traineeUser.username = :#{#searchCriteria.username}
            """)
    List<Training> getTraineeTrainingsByCriteria(@Param("searchCriteria") TraineeTrainingFilter searchCriteria);
}
