package com.epam.controller;

import com.epam.model.TrainingType;
import com.epam.service.TrainingTypeService;
import com.epam.utility.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/training-types")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<List<TrainingType>> getAllTrainingTypes(@RequestHeader HttpHeaders headers) {
        authUtil.validateUser(headers);
        List<TrainingType> trainingTypes = trainingTypeService.getAllTrainingTypes();
        return ResponseEntity.ok(trainingTypes);
    }
}
