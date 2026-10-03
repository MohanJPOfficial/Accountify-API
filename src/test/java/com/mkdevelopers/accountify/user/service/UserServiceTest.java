package com.mkdevelopers.accountify.user.service;

import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private MockedStatic<SecurityUtils> security;
    private UserService userService;

    @BeforeEach
    void setUp() {
        security = mockStatic(SecurityUtils.class);
        security.when(SecurityUtils::getCurrentUserId).thenReturn("user-a");
        userService = new UserService(userRepository);
    }

    @AfterEach
    void tearDown() {
        security.close();
    }

    @Test
    void createRequiresEmailClaim() {
        security.when(() -> SecurityUtils.getClaim(eq("email"), anyString())).thenReturn("");
        when(userRepository.findById("user-a")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, userService::ensureCurrentUser);
    }
}
