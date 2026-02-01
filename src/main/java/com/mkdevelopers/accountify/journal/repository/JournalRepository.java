package com.mkdevelopers.accountify.journal.repository;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.journal.entity.JournalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<JournalEntity, String> {

    Page<JournalEntity> findByBusiness(BusinessEntity business, Pageable pageable);
}
