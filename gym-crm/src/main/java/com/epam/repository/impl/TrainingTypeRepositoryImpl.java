package com.epam.repository.impl;


import com.epam.model.TrainingType;
import com.epam.repository.TrainingTypeRepository;
import org.springframework.stereotype.Repository;


@Repository
public class TrainingTypeRepositoryImpl extends GenericRepositoryImpl<TrainingType, Integer> implements TrainingTypeRepository {
}
