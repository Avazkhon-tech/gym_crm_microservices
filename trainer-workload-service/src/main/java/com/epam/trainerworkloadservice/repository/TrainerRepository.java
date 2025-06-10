package com.epam.trainerworkloadservice.repository;

import com.epam.trainerworkloadservice.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, String> {}
