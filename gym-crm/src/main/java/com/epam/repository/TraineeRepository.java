package com.epam.repository;


import com.epam.model.Trainee;
import com.epam.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends GenericRepository<Trainee, Long> {

    Boolean existsByUsername(String username);

    Optional<Trainee> findByUsername(String username);

    List<Trainer> getActiveUnassignedTrainers(String traineeUse);

}
