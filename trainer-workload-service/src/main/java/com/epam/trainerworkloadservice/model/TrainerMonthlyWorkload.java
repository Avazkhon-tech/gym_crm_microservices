package com.epam.trainerworkloadservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerMonthlyWorkload {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainer_username")
    private Trainer trainer;

    @Column(nullable = false, name = "`year`")
    private int year;

    @Column(nullable = false, name = "`month`")
    private int month;

    @Column(nullable = false)
    private int totalDurationMinutes;
}
