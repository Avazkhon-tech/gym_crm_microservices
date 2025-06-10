package com.epam.repository;

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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        User user = Instancio.of(User.class)
                .setBlank(Select.field(User::getId)).create();
        this.user = userRepository.save(user);
    }

    @Test
    void findByUsername() {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        assertTrue(byUsername.isPresent());
    }

    @Test
    void existsByUsername() {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        assertTrue(byUsername.isPresent());
    }
}
