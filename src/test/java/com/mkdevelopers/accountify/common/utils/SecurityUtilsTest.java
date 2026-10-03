package com.mkdevelopers.accountify.common.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SecurityUtilsTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void missingAuthenticationIsRejected() {
        SecurityContextHolder.clearContext();

        assertThrows(IllegalStateException.class, SecurityUtils::getCurrentUserId);
    }

    @Test
    void anonymousAuthenticationIsRejected() {
        var anonymous = new AnonymousAuthenticationToken(
                "key",
                "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        SecurityContextHolder.getContext().setAuthentication(anonymous);

        assertThrows(IllegalStateException.class, SecurityUtils::getCurrentUserId);
    }

    @Test
    void blankSubjectIsRejected() {
        var jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject(" ")
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        assertThrows(IllegalStateException.class, SecurityUtils::getCurrentUserId);
    }

    @Test
    void subjectIsTheCurrentUserId() {
        var jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-a")
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        assertEquals("user-a", SecurityUtils.getCurrentUserId());
    }
}
