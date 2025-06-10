package com.epam.trainerworkloadservice.controller;

import com.epam.trainerworkloadservice.dto.TrainerWorkloadDto;
import com.epam.trainerworkloadservice.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/trainer-workload")
public class TrainerWorkloadController {

    private final TrainerWorkloadService trainerWorkloadService;

    @PostMapping
    public ResponseEntity<String> updateTrainerWorkload(@Validated @RequestBody TrainerWorkloadDto trainerWorkloadDto) {
        trainerWorkloadService.updateTrainerWorkload(trainerWorkloadDto);
        return ResponseEntity.ok("Workload has been updated successfully");
    }
}
