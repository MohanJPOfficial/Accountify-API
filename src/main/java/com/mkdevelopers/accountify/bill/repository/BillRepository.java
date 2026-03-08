package com.mkdevelopers.accountify.bill.repository;

import com.mkdevelopers.accountify.bill.entity.BillEntity;
import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, String> {
    Page<BillEntity> findByBusiness(BusinessEntity business, Pageable pageable);

    Page<BillEntity> findByLedger(LedgerEntity ledger, Pageable pageable);
}
