package com.epam.controller;

import com.epam.dto.auth.CredentialsUpdateDto;
import com.epam.dto.auth.LoginDto;
import com.epam.exception.AuthenticationException;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.security.JwtProvider;
import com.epam.security.TestSecurityConfig;
import com.epam.service.AuthService;
import com.epam.service.RefreshTokenService;
import com.epam.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthService authService;

    @MockitoBean
    UserService userService;

    @MockitoBean
    RefreshTokenService refreshTokenService;

    @MockitoBean
    JwtProvider jwtProvider;

    @Nested
    class AuthenticateTests {

        @Test
        void shouldAuthenticateSuccessfully() throws Exception {
            LoginDto loginDto = Instancio.create(LoginDto.class);

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDto)))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldNotAuthenticateWhenUserNotFound() throws Exception {
            LoginDto loginDto = Instancio.create(LoginDto.class);
            doThrow(new AuthenticationException("User not found")).when(authService).authenticate(any(LoginDto.class));

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDto)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class UpdatePasswordTests {

        @Test
        @WithMockUser(username = "test")
        void shouldUpdatePasswordSuccessfully() throws Exception {
            CredentialsUpdateDto credentials = Instancio.create(CredentialsUpdateDto.class);

            doNothing().when(userService).updatePassword(any(CredentialsUpdateDto.class));

            mockMvc.perform(put("/auth/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(credentials))
                            .with(user(credentials.username())))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldThrowExceptionIfOldPasswordIsIncorrect() throws Exception {
            CredentialsUpdateDto credentials = Instancio.create(CredentialsUpdateDto.class);

            doThrow(new AuthenticationException("Old password is incorrect"))
                    .when(userService).updatePassword(any(CredentialsUpdateDto.class));

            mockMvc.perform(put("/auth/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(credentials)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldThrowExceptionIfPasswordIsTooShort() throws Exception {
            CredentialsUpdateDto credentials = Instancio.create(CredentialsUpdateDto.class);

            doThrow(new AuthenticationException("Password has to be at least 6 characters"))
                    .when(userService).updatePassword(any(CredentialsUpdateDto.class));

            mockMvc.perform(put("/auth/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(credentials)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldThrowExceptionIfUserDoesNotExist() throws Exception {
            CredentialsUpdateDto credentials = Instancio.create(CredentialsUpdateDto.class);

            doThrow(new EntityDoesNotExistException("User", "username", credentials.username()))
                    .when(userService).updatePassword(any(CredentialsUpdateDto.class));

            mockMvc.perform(put("/auth/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(credentials))
                            .with(user(credentials.username())))
                    .andExpect(status().isNotFound());
        }
    }
}
