package com.epam.controller;

import com.epam.dto.response.ResponseMessage;
import com.epam.dto.trainee.TraineeTrainingFilter;
import com.epam.dto.trainer.TrainerTrainingFilter;
import com.epam.dto.tranining.TraineeTrainingDto;
import com.epam.dto.tranining.TrainerTrainingDto;
import com.epam.dto.tranining.TrainingCreateDto;
import com.epam.service.TrainingService;
import com.epam.utility.CurrentUserAccessor;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;

    @Operation(summary = "Create a new training session")
    @PostMapping
    public ResponseMessage createTraining(
            @Valid @RequestBody TrainingCreateDto trainingCreateDto) {
        trainingService.createTraining(trainingCreateDto);
        return new ResponseMessage("OK");
    }

    @Operation(summary = "Get trainer's trainings with optional filters")
    @GetMapping("/trainer/{username}")
    public ResponseEntity<List<TrainerTrainingDto>> getTrainerTrainings(
            @PathVariable(name = "username") String username,
            @RequestParam(name = "fromDate", required = false) LocalDate fromDate,
            @RequestParam(name = "toDate", required = false) LocalDate toDate,
            @RequestParam(name = "traineeName", required = false) String traineeName) {
        CurrentUserAccessor.validateCurrentUser(username);

        TrainerTrainingFilter filter = TrainerTrainingFilter
                .builder()
                .username(username)
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeName(traineeName)
                .build();

        List<TrainerTrainingDto> trainings = trainingService.getTrainerTrainingsByCriteria(filter);
        return ResponseEntity.ok(trainings);
    }

    @Operation(summary = "Get trainee's trainings with optional filters")
    @GetMapping("/trainee/{username}")
    public ResponseEntity<List<TraineeTrainingDto>> getTraineeTrainings(
            @PathVariable(name = "username") String username,
            @RequestParam(name = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate", required = false) LocalDate toDate,
            @RequestParam(name = "trainerName", required = false) String trainerName) {
        CurrentUserAccessor.validateCurrentUser(username);

        TraineeTrainingFilter filter = TraineeTrainingFilter
                .builder()
                .username(username)
                .fromDate(fromDate)
                .toDate(toDate)
                .trainerName(trainerName)
                .build();

        List<TraineeTrainingDto> trainings = trainingService.getTraineeTrainingsByCriteria(filter);
        return ResponseEntity.ok(trainings);
    }
}
