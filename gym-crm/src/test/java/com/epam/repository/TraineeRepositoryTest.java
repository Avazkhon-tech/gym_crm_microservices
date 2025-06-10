package com.epam.repository;

import com.epam.model.Trainee;
import com.epam.model.User;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class TraineeRepositoryTest {

    @Autowired
    private TraineeRepository traineeRepository;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        Trainee trainee = Instancio.of(Trainee.class)
                .setBlank(Select.field(Trainee::getTrainings))
                .setBlank(Select.field(Trainee::getTrainers))
                .setBlank(Select.field(Trainee::getId))
                .setBlank(Select.field(User::getId)).create();
        this.trainee = traineeRepository.save(trainee);
    }

    @Test
    void findByUsername() {
        Optional<Trainee> byUsername = traineeRepository.findByUsername(trainee.getUser().getUsername());
        assertTrue(byUsername.isPresent());
    }

    @Test
    void existsByUsername() {
        Optional<Trainee> byUsername = traineeRepository.findByUsername(trainee.getUser().getUsername());
        assertTrue(byUsername.isPresent());
    }
}
