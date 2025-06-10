package com.epam.repository.impl;


import com.epam.model.Trainee;
import com.epam.model.Trainer;
import com.epam.repository.TraineeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TraineeRepositoryImpl extends GenericRepositoryImpl<Trainee, Long> implements TraineeRepository {

    @Override
    public Boolean existsByUsername(String username) {
        return entityManager.createQuery(
                        """
                                SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END 
                                FROM Trainee t 
                                JOIN t.user u 
                                WHERE u.username = :username
                                """, Boolean.class)
                .setParameter("username", username)
                .getSingleResult();
    }


    public Optional<Trainee> findByUsername(String username) {

        Trainee trainee = entityManager.createQuery(
                        """
                                SELECT t FROM Trainee t
                                JOIN FETCH t.user u
                                LEFT JOIN FETCH t.trainers tr
                                LEFT JOIN FETCH tr.user trusr
                                LEFT JOIN  FETCH tr.specialization
                                WHERE u.username = :username
                                """, Trainee.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
        return Optional.ofNullable(trainee);
    }

    public List<Trainer> getActiveUnassignedTrainers(String traineeUsername) {
        return entityManager.createQuery(
                        """
                                SELECT t FROM Trainer t
                                JOIN FETCH t.user u
                                JOIN FETCH t.specialization
                                WHERE t.user.username NOT IN (
                                    SELECT t.user.username FROM Trainer t
                                    JOIN t.trainees tr
                                    WHERE tr.user.username = :username
                                )
                                """, Trainer.class
                ).setParameter("username", traineeUsername)
                .getResultList();
    }
}
