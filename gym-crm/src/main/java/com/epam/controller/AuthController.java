package com.epam.controller;

import com.epam.dto.auth.CredentialsUpdateDto;
import com.epam.dto.auth.LoginDto;
import com.epam.service.AuthService;
import com.epam.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody @Valid LoginDto loginDto) throws AuthenticationException {
        authService.authenticate(loginDto);
        return ResponseEntity.ok("OK");
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid CredentialsUpdateDto credentialsUpdateDto) {
        userService.updatePassword(credentialsUpdateDto);
            return ResponseEntity.ok("OK");
    }
}
