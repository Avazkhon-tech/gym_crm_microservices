package com.epam.service;

import com.epam.dto.auth.LoginDto;
import com.epam.exception.AuthenticationException;
import com.epam.model.User;
import com.epam.repository.UserRepository;
import com.epam.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Spy
    private BruteForceDefenseService bruteForceDefenseService;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User user;
    private LoginDto validCredentials;
    private LoginDto invalidPasswordCredentials;
    private LoginDto nonExistentUserCredentials;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("user");
        user.setPassword("password");

        validCredentials = new LoginDto("user", "password");
        invalidPasswordCredentials = new LoginDto("user", "wrongpassword");
        nonExistentUserCredentials = new LoginDto("unknownuser", "password");
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "password")).thenReturn(true);
        assertDoesNotThrow(() -> authService.authenticate(validCredentials));
    }

    @Test
    void authenticate_Failure_InvalidPassword() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                authService.authenticate(invalidPasswordCredentials)
        );

        assertEquals("Username or password is incorrect", exception.getMessage());
    }

    @Test
    void authenticate_Failure_UserNotFound() {
        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                authService.authenticate(nonExistentUserCredentials)
        );

        assertEquals("Username or password is incorrect", exception.getMessage());
    }
}
