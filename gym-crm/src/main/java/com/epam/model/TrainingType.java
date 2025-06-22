package com.epam.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "training_type")
@AllArgsConstructor
@NoArgsConstructor
public class TrainingType {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;
}
