package com.epam.repository;

import com.epam.model.Trainer;
import com.epam.model.User;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class TrainerRepositoryTest {

    @Autowired
    private TrainerRepository trainerRepository;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        Trainer trainer = Trainer.builder()
                .user(Instancio.of(User.class).setBlank(Select.field(User::getId)).create())
                .build();
        this.trainer = trainerRepository.save(trainer);
    }

    @Test
    void findByUsername() {
        Optional<Trainer> byUsername = trainerRepository.findByUsername(trainer.getUser().getUsername());
        assertTrue(byUsername.isPresent());
    }

    @Test
    void findAllByUsernames() {
        List<Trainer> allByUsernames = trainerRepository.findAllByUsernames(List.of(trainer.getUser().getUsername()));
        assertFalse(allByUsernames.isEmpty());
    }
}
