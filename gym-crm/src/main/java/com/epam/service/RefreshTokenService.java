package com.epam.service;

import com.epam.exception.AuthenticationException;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.model.RefreshToken;
import com.epam.model.User;
import com.epam.repository.RefreshTokenRepository;
import com.epam.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_EXPIRATION_MS = 30 * 24 * 60 * 60 * 1000L;
    private static final int MAX_SESSIONS_PER_USER = 3;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public String createRefreshToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityDoesNotExistException("User", "username", username));


        List<RefreshToken> activeRefreshTokens = refreshTokenRepository.findAllByUserId(user.getId());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION_MS))
                .build();

        activeRefreshTokens.add(refreshToken);

        if (activeRefreshTokens.size() > MAX_SESSIONS_PER_USER) {
            List<RefreshToken> list = activeRefreshTokens.stream()
                    .sorted(Comparator.comparing(RefreshToken::getExpiryDate))
                    .toList();

            refreshTokenRepository.deleteAll(list.subList(0, list.size() - MAX_SESSIONS_PER_USER));
        }

        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    public RefreshToken verifyExpiration(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthenticationException("Invalid refresh token has been provided"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new AuthenticationException("You session is expired. Please login again.");
        }

        return refreshToken;
    }
}
