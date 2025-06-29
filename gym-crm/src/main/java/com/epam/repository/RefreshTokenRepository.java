package com.epam.repository;

import com.epam.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    List<RefreshToken> findAllByUserId(Long userId);

    int deleteByExpiryDateBefore(Instant datetime);

    void deleteByUserId(Long userId);
}