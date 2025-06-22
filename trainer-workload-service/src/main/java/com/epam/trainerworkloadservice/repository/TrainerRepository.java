package com.epam.trainerworkloadservice.repository;

import com.epam.trainerworkloadservice.model.Trainer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainerRepository extends MongoRepository<Trainer, String> {

    Optional<Trainer> findByUsername(String username);
}