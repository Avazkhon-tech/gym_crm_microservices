package com.epam.service;

import com.epam.dto.auth.CredentialsUpdateDto;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.exception.AuthenticationException;
import com.epam.model.User;
import com.epam.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User createUser() {
        return User.builder()
                .firstname("Ahmad")
                .lastname("Valiyev")
                .username("Ahmad.Valiyev")
                .password("oldPassword")
                .isActive(true)
                .build();
    }

    private CredentialsUpdateDto createCredentialsUpdateDto(String username, String oldPassword, String newPassword) {
        return new CredentialsUpdateDto(username, oldPassword, newPassword);
    }

    @Nested
    class GenerateUniqueUsernameTests {

        @Test
        void shouldGenerateUniqueUsername() {
            String firstname = "Ahmad";
            String lastname = "Valiyev";

            when(userRepository.existsByUsername("ahmad.valiyev")).thenReturn(false);

            String username = userService.getUniqueUsername(firstname, lastname);

            assertEquals("ahmad.valiyev", username);
        }

        @Test
        void shouldGenerateIncrementedUniqueUsernameWhenConflictExists() {
            String firstname = "Ahmad";
            String lastname = "Valiyev";

            when(userRepository.existsByUsername("ahmad.valiyev")).thenReturn(true);
            when(userRepository.existsByUsername("ahmad.valiyev1")).thenReturn(true);
            when(userRepository.existsByUsername("ahmad.valiyev2")).thenReturn(false);

            String username = userService.getUniqueUsername(firstname, lastname);

            assertEquals("ahmad.valiyev2", username);
        }
    }

    @Nested
    class UpdatePasswordTests {

        @Test
        void shouldUpdatePasswordSuccessfully() {
            User user = createUser();
            CredentialsUpdateDto credentials = createCredentialsUpdateDto(
                    user.getUsername(),
                    user.getPassword(),
                    "newPassword");

            when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

            userService.updatePassword(credentials);

            assertEquals("newPassword", user.getPassword());
        }

        @Test
        void shouldThrowExceptionWhenOldPasswordIsIncorrect() {
            User user = createUser();
            CredentialsUpdateDto credentials = createCredentialsUpdateDto(
                    user.getUsername(),
                    "wrongPassword",
                    "newPassword");

            when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

            assertThrows(AuthenticationException.class, () ->
                    userService.updatePassword(credentials));
        }

        @Test
        void shouldThrowExceptionWhenUserDoesNotExist() {
            User user = createUser();

            when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

            CredentialsUpdateDto invalidCredentials = createCredentialsUpdateDto(
                    user.getUsername(),
                    "wrongPassword",
                    "newPassword");

            assertThrows(EntityDoesNotExistException.class,
                    () -> userService.updatePassword(invalidCredentials));

        }

        @Test
        void shouldThrowExceptionWhenPasswordIsNotComplexEnough() {
            User user = createUser();
            CredentialsUpdateDto invalidCredentials = createCredentialsUpdateDto(user.getUsername(), user.getPassword(), "1234");

            when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> userService.updatePassword(invalidCredentials));

            String expectedMessage = "Password has to be at least 6 characters";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);

        }
    }

    @Test
    void shouldGeneratePasswordSuccessfully() {
        String password = userService.generatePassword();

        assertTrue(password.length() >= 10);
        assertNotNull(password);
        assertEquals(10, password.length());
    }
}
