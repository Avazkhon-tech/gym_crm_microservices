package com.epam.repository.impl;

import com.epam.model.Trainer;
import com.epam.repository.TrainerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerRepositoryImpl extends GenericRepositoryImpl<Trainer, Long> implements TrainerRepository {

    @Override
    public Optional<Trainer> findByUsername(String username) {
        Trainer trainer = entityManager.createQuery(
                        """
                                SELECT t FROM Trainer t
                                JOIN FETCH t.user u
                                JOIN FETCH t.specialization
                                LEFT JOIN FETCH t.trainees tr
                                LEFT JOIN FETCH tr.user
                                WHERE u.username = :username
                                """,
                        Trainer.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
        return Optional.ofNullable(trainer);
    }

    @Override
    public List<Trainer> findAllByUsername(List<String> usernames) {
        return entityManager.createQuery(
                        "SELECT t FROM Trainer t JOIN t.user u WHERE u.username IN :usernames",
                        Trainer.class)
                .setParameter("usernames", usernames)
                .getResultList();
    }

    @Override
    public Boolean existsByUsername(String username) {
        return entityManager.createQuery(
                        """
                                SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END
                                FROM Trainer t
                                JOIN t.user u
                                WHERE u.username = :username
                                """, Boolean.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
