package com.epam.service;

import com.epam.dto.auth.LoginDto;
import com.epam.model.User;
import com.epam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void authenticate(LoginDto credentials) throws AuthenticationException {
        User user = userRepository.findByUsername(credentials.username()).orElse(null);

        if (Objects.isNull(user) || !user.getPassword().equals(credentials.password())) {
            throw new AuthenticationException("Username or password is incorrect");
        }
    }
}