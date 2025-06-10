package com.epam.controller;

import com.epam.dto.auth.LoginDto;
import com.epam.dto.trainer.TrainerProfileDto;
import com.epam.dto.trainer.TrainerProfileUpdateDto;
import com.epam.dto.trainer.TrainerRegistrationDto;
import com.epam.service.TrainerService;
import com.epam.utility.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final AuthUtil authUtil;

    @PostMapping("/register")
    public ResponseEntity<LoginDto> registerTrainer(@RequestBody @Valid TrainerRegistrationDto trainerRegistrationRequest) {
        LoginDto trainerCredentials = trainerService.createTrainer(trainerRegistrationRequest);
        return ResponseEntity.ok(trainerCredentials);
    }

    @GetMapping("/{username}")
    public TrainerProfileDto getTrainerProfile(
            @RequestHeader HttpHeaders headers,
            @PathVariable(name = "username") String username) {
        authUtil.validateUser(headers);
        return trainerService.getTrainerProfileByUsername(username);
    }

    @PutMapping
    public TrainerProfileDto updateTrainerProfile(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid TrainerProfileUpdateDto trainerProfileUpdateDto) {
        authUtil.validateUser(headers);
        return trainerService.updateTrainer(trainerProfileUpdateDto);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<String> updateTrainerActiveStatus(
            @RequestHeader HttpHeaders headers,
            @PathVariable(name = "username") String username,
            @RequestParam("isActive") Boolean isActive) {
        authUtil.validateUser(headers);
        trainerService.updateActiveStatus(username, isActive);
        return ResponseEntity.ok("OK");
    }
}
