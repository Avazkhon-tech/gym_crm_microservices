package com.epam.mapper;


import com.epam.dto.tranining.TraineeTrainingDto;
import com.epam.dto.tranining.TrainerTrainingDto;
import com.epam.dto.tranining.TrainingCreateDto;
import com.epam.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainee", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    @Mapping(target = "trainingType", ignore = true)
    Training toEntity(TrainingCreateDto trainingCreateDtoDto);

    @Mapping(target = "trainingType", source = "trainingType.name")
    @Mapping(target = "traineeName", source = "trainee.user.firstname")
    TrainerTrainingDto toTrainerTrainingDto(Training training);

    @Mapping(target = "trainingType", source = "trainingType.name")
    @Mapping(target = "trainerName", source = "trainer.user.firstname")
    TraineeTrainingDto toTraineeTrainingDto(Training training);

}
