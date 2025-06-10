package com.epam.controller;

import com.epam.dto.trainee.TraineeTrainingFilter;
import com.epam.dto.trainer.TrainerTrainingFilter;
import com.epam.dto.tranining.TraineeTrainingDto;
import com.epam.dto.tranining.TrainerTrainingDto;
import com.epam.dto.tranining.TrainingCreateDto;
import com.epam.service.TrainingService;
import com.epam.utility.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;
    private final AuthUtil authUtil;

    @PostMapping
    public ResponseEntity<String> createTraining(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody TrainingCreateDto trainingCreateDto) {
        authUtil.validateUser(headers);
        trainingService.createTraining(trainingCreateDto);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/trainer/{username}")
    public ResponseEntity<List<TrainerTrainingDto>> getTrainerTrainings(
            @RequestHeader HttpHeaders headers,
            @PathVariable(name = "username") String username,
            @RequestParam(name = "fromDate", required = false) LocalDate fromDate,
            @RequestParam(name = "toDate", required = false) LocalDate toDate,
            @RequestParam(name = "traineeName", required = false) String traineeName) {

        authUtil.validateUser(headers);

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

    @GetMapping("/trainee/{username}")
    public ResponseEntity<List<TraineeTrainingDto>> getTraineeTrainings(
            @RequestHeader HttpHeaders headers,
            @PathVariable(name = "username") String username,
            @RequestParam(name = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate", required = false) LocalDate toDate,
            @RequestParam(name = "trainerName", required = false) String trainerName) {

        authUtil.validateUser(headers);

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
