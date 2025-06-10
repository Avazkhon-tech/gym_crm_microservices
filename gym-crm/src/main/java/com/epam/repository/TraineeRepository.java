package com.epam.repository;


import com.epam.model.Trainee;
import com.epam.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END
            FROM Trainee t
            JOIN t.user u
            WHERE u.username = :username
            """)
    Boolean existsByUsername(@Param("username") String username);

    @Query("""
            SELECT t FROM Trainee t
            JOIN FETCH t.user u
            LEFT JOIN FETCH t.trainers tr
            LEFT JOIN FETCH tr.user trusr
            LEFT JOIN  FETCH tr.specialization
            WHERE u.username = :username
            """)
    Optional<Trainee> findByUsername(@Param("username") String username);

    @Query("""
            SELECT t FROM Trainer t
            JOIN FETCH t.user u
            JOIN FETCH t.specialization
            WHERE t.user.username NOT IN (
                SELECT t.user.username FROM Trainer t
                JOIN t.trainees tr
                WHERE tr.user.username = :username
            )
            """)
    List<Trainer> getActiveUnassignedTrainers(@Param("username") String traineeUsername);

}
