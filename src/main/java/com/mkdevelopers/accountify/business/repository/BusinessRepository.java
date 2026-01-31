package com.mkdevelopers.accountify.business.repository;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessRepository extends JpaRepository<BusinessEntity, String> {

    List<BusinessEntity> findByUser(UserEntity user);

    Page<BusinessEntity> findByUser(UserEntity user, Pageable pageable);
}
