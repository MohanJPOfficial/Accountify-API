package com.mkdevelopers.accountify.common.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * Extracts the securely cryptographically verified user ID (Firebase UID)
     * from the current request's JWT token.
     */
    public static String getCurrentUserId() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new IllegalStateException("User is not authenticated");
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
