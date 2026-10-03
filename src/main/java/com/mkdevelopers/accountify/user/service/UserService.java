package com.mkdevelopers.accountify.user.service;

import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.user.entity.UserEntity;
import com.mkdevelopers.accountify.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserEntity ensureCurrentUser() {
        String userId = SecurityUtils.getCurrentUserId();
        return userRepository.findById(userId).orElseGet(() -> {
            var user = new UserEntity();
            user.setUserId(userId);
            user.setEmail(SecurityUtils.getClaim("email", userId + "@users.accountify.local"));
            user.setProfileName(SecurityUtils.getClaim("name", "User"));
            return userRepository.save(user);
        });
    }
}
