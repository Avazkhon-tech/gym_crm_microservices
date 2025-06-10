package com.epam.repository;

import com.epam.dto.trainee.TraineeTrainingFilter;
import com.epam.dto.trainer.TrainerTrainingFilter;
import com.epam.model.Training;

import java.util.List;

public interface TrainingRepository extends GenericRepository<Training, Long> {

    List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter searchCriteria);

    List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter searchCriteria);
}
