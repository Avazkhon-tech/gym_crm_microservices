package com.epam.utility;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class PublicEndpoints {

    private final Set<RequestMatcher> matchers = new HashSet<>(List.of(
            new AntPathRequestMatcher("/trainees/register", "POST"),
            new AntPathRequestMatcher("/trainers/register", "POST"),
            new AntPathRequestMatcher("/auth/login", "POST"),
            new AntPathRequestMatcher("/auth/refresh-token", "POST"),
            new AntPathRequestMatcher("/auth/logout", "POST")
    ));

    public boolean shouldSkip(HttpServletRequest request) {
        return matchers.stream().anyMatch(matcher -> matcher.matches(request));
    }
}