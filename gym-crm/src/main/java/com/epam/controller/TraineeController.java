package com.epam.controller;

import com.epam.dto.auth.LoginDto;
import com.epam.dto.trainee.TraineeProfileDto;
import com.epam.dto.trainee.TraineeProfileUpdateDto;
import com.epam.dto.trainee.TraineeRegistrationDto;
import com.epam.dto.trainee.TraineeTrainerDto;
import com.epam.model.User;
import com.epam.service.TraineeService;
import com.epam.service.UserService;
import com.epam.utility.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainees")
public class TraineeController {

    private final TraineeService traineeService;
    private final UserService userService;
    private final AuthUtil authUtil;

    @PostMapping("/register")
    public ResponseEntity<LoginDto> registerTrainee(
            @RequestBody @Valid TraineeRegistrationDto traineeRegistrationDto) {
        LoginDto traineeCredentials = traineeService.createTrainee(traineeRegistrationDto);
        return ResponseEntity.ok(traineeCredentials);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileDto> getTraineeProfile(
            @RequestHeader HttpHeaders headers,
            @PathVariable(name = "username") String username) {
        authUtil.validateUser(headers);
        TraineeProfileDto traineeDto = traineeService.getTraineeProfile(username);
        return ResponseEntity.ok(traineeDto);
    }

    @GetMapping("/{username}/unassigned-trainers")
    public ResponseEntity<List<TraineeTrainerDto>> getActiveUnassignedTrainers(
            @RequestHeader HttpHeaders headers,
            @PathVariable(name = "username") String username) {
        authUtil.validateUser(headers);
        List<TraineeTrainerDto> unassignedTrainers = traineeService.getActiveUnassignedTrainers(username);
        return ResponseEntity.ok(unassignedTrainers);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileDto> updateTrainee(
            @RequestHeader HttpHeaders headers,
            @PathVariable(name = "username") String username,
            @RequestBody @Valid TraineeProfileUpdateDto traineeProfileUpdateDto) {
        authUtil.validateUser(headers);
        TraineeProfileDto traineeDto = traineeService.updateTrainee(username, traineeProfileUpdateDto);
        return ResponseEntity.ok(traineeDto);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteTrainee(
            @RequestHeader HttpHeaders headers,
            @PathVariable(name = "username") String username) {
        authUtil.validateUser(headers);
        traineeService.deleteTrainee(username);
        return ResponseEntity.ok("OK");
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TraineeTrainerDto>> updateTraineeTrainers(
            @RequestHeader HttpHeaders headers,
            @PathVariable(name = "username") String username,
            @RequestBody List<String> usernames) {
        authUtil.validateUser(headers);
        List<TraineeTrainerDto> trainers = traineeService.updateTraineeTrainers(username, usernames);
        return ResponseEntity.ok(trainers);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<String> updateActiveStatus(
            @RequestHeader HttpHeaders headers,
            @PathVariable(name = "username") String username,
            @RequestParam("isActive") Boolean isActive) {
        authUtil.validateUser(headers);
        traineeService.updateActiveStatus(username, isActive);
        return ResponseEntity.ok("OK");
    }
}
