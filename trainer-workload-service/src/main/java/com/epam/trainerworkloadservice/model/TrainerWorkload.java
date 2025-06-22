package com.epam.trainerworkloadservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "trainer_workloads")
@CompoundIndex(name = "trainerUsername-trainingdate", def = "{'trainerUsername': 1, 'trainingDate': 1}")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkload {

    @Id
    private String id;

    private String trainerUsername;

    private LocalDate trainingDate;

    private int trainingDurationMinutes;
}
