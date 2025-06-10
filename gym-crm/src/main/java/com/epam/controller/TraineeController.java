package com.epam.controller;

import com.epam.dto.auth.LoginDto;
import com.epam.dto.response.ResponseMessage;
import com.epam.dto.trainee.TraineeProfileDto;
import com.epam.dto.trainee.TraineeProfileUpdateDto;
import com.epam.dto.trainee.TraineeRegistrationDto;
import com.epam.dto.trainee.TraineeTrainerDto;
import com.epam.service.TraineeService;
import com.epam.utility.CurrentUserAccessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainees")
public class TraineeController {

    private final TraineeService traineeService;

    @Operation(summary = "Register a new trainee")
    @PostMapping("/register")
    public ResponseEntity<LoginDto> registerTrainee(
            @Valid @RequestBody TraineeRegistrationDto traineeRegistrationDto) {
        LoginDto traineeCredentials = traineeService.createTrainee(traineeRegistrationDto);
        return ResponseEntity.ok(traineeCredentials);
    }

    @Operation(summary = "Get trainee profile by username")
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileDto> getTraineeProfile(
            @Parameter(description = "Username of the trainee", required = true)
            @PathVariable(name = "username") String username) {
        CurrentUserAccessor.validateCurrentUser(username);
        TraineeProfileDto traineeDto = traineeService.getTraineeProfile(username);
        return ResponseEntity.ok(traineeDto);
    }

    @Operation(summary = "Get active unassigned trainers for a trainee")
    @GetMapping("/{username}/unassigned-trainers")
    public ResponseEntity<List<TraineeTrainerDto>> getActiveUnassignedTrainers(
            @Parameter(description = "Username of the trainee", required = true)
            @PathVariable(name = "username") String username) {
        CurrentUserAccessor.validateCurrentUser(username);
        List<TraineeTrainerDto> unassignedTrainers = traineeService.getActiveUnassignedTrainers(username);
        return ResponseEntity.ok(unassignedTrainers);
    }

    @Operation(summary = "Update trainee profile")
    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileDto> updateTrainee(
            @Parameter(description = "Username of the trainee to update", required = true)
            @PathVariable(name = "username") String username,
            @Valid @RequestBody TraineeProfileUpdateDto traineeProfileUpdateDto) {
        CurrentUserAccessor.validateCurrentUser(username);
        TraineeProfileDto traineeDto = traineeService.updateTrainee(username, traineeProfileUpdateDto);
        return ResponseEntity.ok(traineeDto);
    }

    @Operation(summary = "Delete a trainee")
    @DeleteMapping("/{username}")
    public ResponseMessage deleteTrainee(
            @Parameter(description = "Username of the trainee to delete", required = true)
            @PathVariable(name = "username") String username) {
        CurrentUserAccessor.validateCurrentUser(username);
        traineeService.deleteTrainee(username);
        return new ResponseMessage("OK");
    }

    @Operation(summary = "Update assigned trainers for a trainee")
    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TraineeTrainerDto>> updateTraineeTrainers(
            @Parameter(description = "Username of the trainee", required = true)
            @PathVariable(name = "username") String username,
            @RequestBody List<String> usernames) {
        CurrentUserAccessor.validateCurrentUser(username);
        List<TraineeTrainerDto> trainers = traineeService.updateTraineeTrainers(username, usernames);
        return ResponseEntity.ok(trainers);
    }

    @Operation(summary = "Update active status of a trainee")
    @PatchMapping("/{username}")
    public ResponseMessage updateActiveStatus(
            @Parameter(description = "Username of the trainee", required = true)
            @PathVariable(name = "username") String username,
            @Parameter(description = "New active status", required = true)
            @RequestParam("isActive") Boolean isActive) {
        CurrentUserAccessor.validateCurrentUser(username);
        traineeService.updateActiveStatus(username, isActive);
        return new ResponseMessage("OK");
    }
}
