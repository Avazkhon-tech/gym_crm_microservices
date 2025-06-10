package com.epam.repository;


import com.epam.model.Trainer;

import java.util.List;
import java.util.Optional;


public interface TrainerRepository extends GenericRepository<Trainer, Long>{

    Boolean existsByUsername(String username);

    Optional<Trainer> findByUsername(String username);

    List<Trainer> findAllByUsername(List<String> usernames);

}
