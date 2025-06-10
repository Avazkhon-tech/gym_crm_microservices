package com.epam.service;

import com.epam.dto.auth.CredentialsUpdateDto;
import com.epam.exception.AuthenticationException;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.model.User;
import com.epam.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String generatePassword() {
        return RandomStringUtils.secure().next(10, true, true);
    }


    public String getUniqueUsername(String firstname, String lastname) {

        String base = firstname + "." + lastname;
        String generatedUsername = base.toLowerCase();
        int count = 1;

        while (userRepository.existsByUsername(generatedUsername)) {
            generatedUsername = (base + count++).toLowerCase();
        }

        return generatedUsername;
    }


    @Transactional
    public void updatePassword(CredentialsUpdateDto credentialsUpdateDto) {

        Optional<User> userOptional = userRepository.findByUsername(credentialsUpdateDto.username());

        User user = userOptional.orElseThrow(
                () -> new EntityDoesNotExistException("User", "username", credentialsUpdateDto.username()));

        if (!passwordEncoder.matches(credentialsUpdateDto.oldPassword(), user.getPassword())) {
            throw new AuthenticationException("Old password is incorrect");
        }

        if (credentialsUpdateDto.password().length() < 4) {
            throw new AuthenticationException("Password has to be at least 4 characters long");
        }

        user.setPassword(passwordEncoder.encode(credentialsUpdateDto.password()));
    }
}
