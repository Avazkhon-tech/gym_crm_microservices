package com.epam.mapper;

import com.epam.dto.trainee.TraineeProfileDto;
import com.epam.dto.trainee.TraineeProfileUpdateDto;
import com.epam.dto.trainee.TraineeRegistrationDto;
import com.epam.dto.trainer.TrainerTraineeDto;
import com.epam.model.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = TrainerMapper.class)
public abstract class TraineeMapper {

    @Mapping(target = "user.firstname",  source = "firstname")
    @Mapping(target = "user.lastname",  source = "lastname")
    @Mapping(target = "user.isActive", constant = "true")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainers", ignore = true)
    @Mapping(target = "trainings", ignore = true)
    public abstract Trainee toEntity(TraineeRegistrationDto registrationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainers", ignore = true)
    @Mapping(target = "trainings", ignore = true)
    @Mapping(target = "user.firstname",  source = "firstname")
    @Mapping(target = "user.lastname",  source = "lastname")
    @Mapping(target = "user.isActive",  source = "isActive")
    public abstract void updateEntity(@MappingTarget Trainee entity, TraineeProfileUpdateDto traineeProfileUpdateDto);


    @Mapping(target = "firstname", source = "user.firstname")
    @Mapping(target = "lastname", source = "user.lastname")
    @Mapping(target = "isActive", source = "user.isActive")
    public abstract TraineeProfileDto toTraineeProfileDto(Trainee trainee);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "firstname", source = "user.firstname")
    @Mapping(target = "lastname", source = "user.lastname")
    public abstract TrainerTraineeDto toTraineeTraineeDto(Trainee trainee);
}
