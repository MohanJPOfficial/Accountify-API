package com.mkdevelopers.accountify.ledger.repository;

import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerRepository extends JpaRepository<LedgerEntity, String> {
}
