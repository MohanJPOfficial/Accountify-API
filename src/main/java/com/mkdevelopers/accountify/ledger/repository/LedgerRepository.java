package com.mkdevelopers.accountify.ledger.repository;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerRepository extends JpaRepository<LedgerEntity, String> {
    Page<LedgerEntity> findByBusiness(BusinessEntity business, Pageable pageable);
}
