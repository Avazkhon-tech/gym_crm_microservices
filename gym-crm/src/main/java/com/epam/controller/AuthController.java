package com.epam.controller;

import com.epam.dto.auth.CredentialsUpdateDto;
import com.epam.dto.auth.LoginDto;
import com.epam.dto.auth.RefreshTokenRequest;
import com.epam.dto.auth.Token;
import com.epam.dto.response.ResponseMessage;
import com.epam.model.RefreshToken;
import com.epam.security.JwtProvider;
import com.epam.service.AuthService;
import com.epam.service.RefreshTokenService;
import com.epam.service.UserService;
import com.epam.utility.CurrentUserAccessor;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "Authenticate user and log in")
    @PostMapping("/login")
    public Token authenticate(@RequestBody @Valid LoginDto loginDto) {
        String jwtToken = authService.authenticate(loginDto);
        String refreshToken = refreshTokenService.createRefreshToken(loginDto.username());
        return new Token(jwtToken, refreshToken);
    }

    @Operation(summary = "Update user password")
    @PutMapping("/password")
    public ResponseMessage updatePassword(@RequestBody @Valid CredentialsUpdateDto credentialsUpdateDto) {
        CurrentUserAccessor.validateCurrentUser(credentialsUpdateDto.username());
        userService.updatePassword(credentialsUpdateDto);
        return new ResponseMessage("OK");
    }

    @PostMapping("/refresh-token")
    public Token refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenRequest.refreshToken());
        return new Token(jwtProvider.generateToken(refreshToken.getUser().getUsername()), refreshToken.getToken());
    }

    @PostMapping("/logout")
    public ResponseMessage logout(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.refreshToken());
        return new ResponseMessage("Successfully logged out");
    }


}
