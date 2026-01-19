package com.mkdevelopers.accountify.business.repository;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusinessRepository extends JpaRepository<BusinessEntity, String> {

    @Query("SELECT b FROM BusinessEntity b WHERE b.user = :user")
    List<BusinessEntity> getAllBusinessesByUser(@Param("user") UserEntity user);
}
