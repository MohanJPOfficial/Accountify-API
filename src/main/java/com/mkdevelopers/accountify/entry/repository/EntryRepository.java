package com.mkdevelopers.accountify.entry.repository;

import com.mkdevelopers.accountify.entry.entity.EntryEntity;
import com.mkdevelopers.accountify.journal.entity.JournalEntity;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, String> {
    Page<EntryEntity> findByJournal(JournalEntity journal, Pageable pageable);
    
    Page<EntryEntity> findByLedger(LedgerEntity ledger, Pageable pageable);
}
