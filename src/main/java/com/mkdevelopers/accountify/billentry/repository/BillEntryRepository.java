package com.mkdevelopers.accountify.billentry.repository;

import com.mkdevelopers.accountify.bill.entity.BillEntity;
import com.mkdevelopers.accountify.billentry.entity.BillEntryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillEntryRepository extends JpaRepository<BillEntryEntity, String> {
    Page<BillEntryEntity> findByBill(BillEntity bill, Pageable pageable);
}
