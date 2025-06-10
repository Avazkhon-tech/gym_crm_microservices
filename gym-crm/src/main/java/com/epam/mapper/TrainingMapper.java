package com.epam.mapper;


import com.epam.dto.tranining.TraineeTrainingDto;
import com.epam.dto.tranining.TrainerTrainingDto;
import com.epam.dto.tranining.TrainingCreateDto;
import com.epam.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class TrainingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainee", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    @Mapping(target = "trainingType", ignore = true)
    public abstract Training toEntity(TrainerTrainingDto dto);

    @Mapping(target = "trainingType", source = "trainingType.name")
    @Mapping(target = "traineeName", source = "trainee.user.firstname")
    public abstract TrainerTrainingDto toTrainerTrainingDto(Training training);

    @Mapping(target = "trainingType", source = "trainingType.name")
    @Mapping(target = "trainerName", source = "trainer.user.firstname")
    public abstract TraineeTrainingDto toTraineeTrainingDto(Training training);



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainee", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    @Mapping(target = "trainingType", ignore = true)
    public abstract Training toEntity(TrainingCreateDto trainingCreateDtoDto);
}
