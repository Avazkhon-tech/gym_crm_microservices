package com.epam.mapper;

import com.epam.dto.trainee.TraineeTrainerDto;
import com.epam.dto.trainer.TrainerProfileDto;
import com.epam.dto.trainer.TrainerProfileUpdateDto;
import com.epam.dto.trainer.TrainerRegistrationDto;
import com.epam.model.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = TraineeMapper.class)
public interface TrainerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "specialization", ignore = true)
    @Mapping(target = "trainees", ignore = true)
    @Mapping(target = "user.username", ignore = true)
    @Mapping(target = "user.password", ignore = true)
    @Mapping(target = "user.firstname", source = "firstname")
    @Mapping(target = "user.lastname", source = "lastname")
    void updateEntity(@MappingTarget Trainer entity, TrainerProfileUpdateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainees", ignore = true)
    @Mapping(target = "specialization", ignore = true)
    @Mapping(target = "user.isActive", constant = "true")
    @Mapping(target = "user.firstname", source = "firstname")
    @Mapping(target = "user.lastname", source = "lastname")
    Trainer toEntity(TrainerRegistrationDto trainerRegistrationRequest);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "firstname", source = "user.firstname")
    @Mapping(target = "lastname", source = "user.lastname")
    @Mapping(target = "specialization", source = "specialization.name")
    TraineeTrainerDto toTraineeTrainerDto(Trainer trainer);

    @Mapping(target = "firstname", source = "user.firstname")
    @Mapping(target = "lastname", source = "user.lastname")
    @Mapping(target = "isActive", source = "user.isActive")
    @Mapping(target = "specialization", source = "specialization.name")
    TrainerProfileDto toTrainerProfileDto(Trainer trainer);

}
