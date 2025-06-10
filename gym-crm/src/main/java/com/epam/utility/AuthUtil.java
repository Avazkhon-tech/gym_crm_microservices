package com.epam.utility;

import com.epam.exception.AuthenticationException;
import com.epam.model.User;
import com.epam.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthUtil {

    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static String[] extractCredentials(HttpHeaders headers) {
        System.out.println(headers);
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Missing or invalid Authorization header.");
        }
        String credentialsRaw = authHeader.substring(7);
        String[] credentials = credentialsRaw.split(":", 2);
        if (credentials.length != 2) {
            throw new AuthenticationException("Invalid Authorization format.");
        }

        return credentials;
    }


    public void validateUser(HttpHeaders headers) {
        String[] strings = extractCredentials(headers);
        String username = strings[0];
        String password = strings[1];

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new AuthenticationException("User not found.");
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid password.");
        }

        if (!user.getIsActive()) {
            throw new AuthenticationException("User account is deactivated.");
        }

    }
}
