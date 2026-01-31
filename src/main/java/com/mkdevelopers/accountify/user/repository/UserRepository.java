package com.mkdevelopers.accountify.user.repository;

import com.mkdevelopers.accountify.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
