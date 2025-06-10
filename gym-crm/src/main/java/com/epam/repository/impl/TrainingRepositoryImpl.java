package com.epam.repository.impl;

import com.epam.dto.trainee.TraineeTrainingFilter;
import com.epam.dto.trainer.TrainerTrainingFilter;
import com.epam.model.Training;
import com.epam.repository.TrainingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainingRepositoryImpl extends GenericRepositoryImpl<Training, Long> implements TrainingRepository {

    public List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter searchCriteria) {
        return entityManager.createQuery(
                        """
                        SELECT t FROM Training t
                        JOIN FETCH t.trainingType
                        JOIN FETCH t.trainer trainer
                        JOIN FETCH trainer.user trainerUser
                        JOIN FETCH trainer.specialization
                        JOIN FETCH t.trainee trainee
                        JOIN FETCH trainee.user traineeUser
                        WHERE (:traineeName IS NULL OR LOWER(traineeUser.firstname) LIKE :traineeName)
                        AND (:fromDate IS NULL OR t.trainingDate >= :fromDate)
                        AND (:toDate IS NULL OR t.trainingDate <= :toDate)
                        AND trainerUser.username = :username
                        """, Training.class)
                .setParameter("username", searchCriteria.username())
                .setParameter("traineeName", searchCriteria.traineeName())
                .setParameter("fromDate", searchCriteria.fromDate())
                .setParameter("toDate", searchCriteria.toDate())
                .getResultList();
    }

    public List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter searchCriteria) {
        return entityManager.createQuery(
                        """
                        SELECT t FROM Training t
                        JOIN FETCH t.trainingType
                        JOIN FETCH t.trainer trainer
                        JOIN FETCH trainer.user trainerUser
                        JOIN FETCH trainer.specialization
                        JOIN FETCH t.trainee trainee
                        JOIN FETCH trainee.user traineeUser
                        WHERE (:trainerName IS NULL OR LOWER(trainerUser.firstname) LIKE :trainerName)
                        AND (:fromDate IS NULL OR t.trainingDate >= :fromDate)
                        AND (:toDate IS NULL OR t.trainingDate <= :toDate)
                        AND (:trainingType IS NULL OR t.trainingType.name = :trainingType)
                        AND traineeUser.username = :username
                        """, Training.class)
                .setParameter("username", searchCriteria.username())
                .setParameter("trainerName", searchCriteria.trainerName())
                .setParameter("fromDate", searchCriteria.fromDate())
                .setParameter("toDate", searchCriteria.toDate())
                .setParameter("trainingType", searchCriteria.trainingType())
                .getResultList();
    }
}
