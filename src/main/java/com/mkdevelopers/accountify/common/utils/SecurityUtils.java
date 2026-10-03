package com.mkdevelopers.accountify.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Extracts the securely cryptographically verified user ID (Firebase UID)
     * from the current request's JWT token.
     */
    public static String getCurrentUserId() {
        return currentAuthentication().getName();
    }

    /**
     * Reads a string claim from the current JWT. Returns {@code fallback} when the
     * claim is missing or blank.
     */
    public static String getClaim(String claim, String fallback) {
        if (currentAuthentication().getPrincipal() instanceof Jwt jwt) {
            String value = jwt.getClaimAsString(claim);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return fallback;
    }

    private static Authentication currentAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("User is not authenticated");
        }
        return authentication;
    }
}
