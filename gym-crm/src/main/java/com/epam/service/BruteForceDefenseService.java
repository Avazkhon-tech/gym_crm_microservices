package com.epam.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BruteForceDefenseService {

    @Value("${brute.force.login.max.attempts}")
    private int MAX_FAILED_LOGIN;
    @Value("${brute.force.login.locked.minutes}")
    private int LOCKED_MINUTES;

    private final ConcurrentHashMap<String, FailedLogin> cache = new ConcurrentHashMap<>();

    public boolean isBlocked(String username) {
        FailedLogin failedLogin = cache.get(username);
        if (failedLogin != null && failedLogin.blockedUntil != null) {
                if (failedLogin.blockedUntil.isAfter(LocalDateTime.now())) {
                    return true;
                } else {
                    resetCounter(username);
                }
            }
        return false;
    }

    public void resetCounter(String username) {
        cache.remove(username);
    }

    public void incrementFailedAttempt(String username) {
        cache.compute(username, (key, failedLogin) -> {
            if (failedLogin == null) {
                failedLogin = new FailedLogin();
            }
            failedLogin.count++;
            if (failedLogin.count >= MAX_FAILED_LOGIN) {
                failedLogin.blockedUntil = LocalDateTime.now().plusMinutes(LOCKED_MINUTES);
            }
            return failedLogin;
        });
    }

    private static class FailedLogin {
        private int count = 0;
        private LocalDateTime blockedUntil = null;
    }
}
