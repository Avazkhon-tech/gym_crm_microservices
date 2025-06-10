package com.epam.service;

import com.epam.dto.auth.LoginDto;
import com.epam.exception.AccountBlockedException;
import com.epam.exception.AuthenticationException;
import com.epam.model.User;
import com.epam.repository.UserRepository;
import com.epam.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final BruteForceDefenseService bruteForceDefenseService;

    public String authenticate(LoginDto credentials) {
        if (bruteForceDefenseService.isBlocked(credentials.username())) {
            throw new AccountBlockedException("Your account is locked due to too many failed login attempts");
        }

        User user = userRepository.findByUsername(credentials.username()).orElse(null);

        if (Objects.isNull(user) || !passwordEncoder.matches(credentials.password(), user.getPassword())) {
            bruteForceDefenseService.incrementFailedAttempt(credentials.username());
            throw new AuthenticationException("Username or password is incorrect");
        }

        bruteForceDefenseService.resetCounter(credentials.username());
        return jwtProvider.generateToken(user.getUsername());
    }
}