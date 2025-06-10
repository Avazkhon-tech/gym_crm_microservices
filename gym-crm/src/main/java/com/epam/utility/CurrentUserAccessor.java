package com.epam.utility;

import com.epam.exception.AuthenticationException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;


@UtilityClass
public class CurrentUserAccessor {

    public void validateCurrentUser(String expectedUsername) {
        String actualUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!expectedUsername.equals(actualUsername)) {
            throw new AuthenticationException("Access denied: authenticated user does not match the provided username.");
        }
    }
}