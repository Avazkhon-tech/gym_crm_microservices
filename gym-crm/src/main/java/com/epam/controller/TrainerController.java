package com.epam.controller;

import com.epam.dto.auth.LoginDto;
import com.epam.dto.response.ResponseMessage;
import com.epam.dto.trainer.TrainerProfileDto;
import com.epam.dto.trainer.TrainerProfileUpdateDto;
import com.epam.dto.trainer.TrainerRegistrationDto;
import com.epam.service.TrainerService;
import com.epam.utility.CurrentUserAccessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainers")
@Tag(name = "Trainer", description = "Operations related to trainer management")
public class TrainerController {

    private final TrainerService trainerService;

    @Operation(
            summary = "Register a new trainer",
            description = "Registers a new trainer and returns the trainer's login credentials.")
    @PostMapping("/register")
    public ResponseEntity<LoginDto> registerTrainer(
            @RequestBody @Valid @Parameter(description = "Trainer registration details") TrainerRegistrationDto trainerRegistrationRequest) {
        LoginDto trainerCredentials = trainerService.createTrainer(trainerRegistrationRequest);
        return ResponseEntity.ok(trainerCredentials);
    }

    @Operation(
            summary = "Get trainer profile",
            description = "Fetches the profile of a trainer by their username.")
    @GetMapping("/{username}")
    public TrainerProfileDto getTrainerProfile(
            @PathVariable(name = "username") @Parameter(description = "Username of the trainer") String username) {
        CurrentUserAccessor.validateCurrentUser(username);
        return trainerService.getTrainerProfileByUsername(username);
    }

    @Operation(
            summary = "Update trainer profile",
            description = "Updates the profile details of a trainer."
    )
    @PutMapping
    public TrainerProfileDto updateTrainerProfile(
            @RequestBody @Valid @Parameter(description = "Updated trainer profile details") TrainerProfileUpdateDto trainerProfileUpdateDto) {
        CurrentUserAccessor.validateCurrentUser(trainerProfileUpdateDto.username());
        return trainerService.updateTrainer(trainerProfileUpdateDto);
    }

    @Operation(
            summary = "Update trainer active status",
            description = "Updates the active status of a trainer."
    )
    @PatchMapping("/{username}")
    public ResponseMessage updateTrainerActiveStatus(
            @PathVariable(name = "username") @Parameter(description = "Username of the trainer") String username,
            @RequestParam("isActive") @Parameter(description = "New active status") Boolean isActive) {
        CurrentUserAccessor.validateCurrentUser(username);
        trainerService.updateActiveStatus(username, isActive);
        return new ResponseMessage("OK");
    }
}

