package com.mkdevelopers.accountify.entry.service;

import com.mkdevelopers.accountify.entry.dto.EntryDto;
import com.mkdevelopers.accountify.entry.dto.JournalEntryRequest;
import com.mkdevelopers.accountify.entry.dto.LedgerEntryRequest;
import com.mkdevelopers.accountify.entry.dto.UpdateJournalEntryRequest;
import com.mkdevelopers.accountify.entry.dto.UpdateLedgerEntryRequest;
import com.mkdevelopers.accountify.entry.exception.DuplicateEntryException;
import com.mkdevelopers.accountify.entry.exception.EntryNotFoundException;
import com.mkdevelopers.accountify.entry.mapper.EntryMapper;
import com.mkdevelopers.accountify.entry.repository.EntryRepository;
import com.mkdevelopers.accountify.journal.exception.JournalNotFoundException;
import com.mkdevelopers.accountify.journal.repository.JournalRepository;
import com.mkdevelopers.accountify.ledger.enums.LedgerParticular;
import com.mkdevelopers.accountify.ledger.exception.LedgerNotFoundException;
import com.mkdevelopers.accountify.ledger.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.mkdevelopers.accountify.common.utils.Ownership;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.entry.entity.EntryEntity;
import com.mkdevelopers.accountify.journal.entity.JournalEntity;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;

@RequiredArgsConstructor
@Service
@Transactional
public class EntryService {

    // Removed hardcoded uuid-007

    private final EntryRepository entryRepository;
    private final EntryMapper entryMapper;
    private final JournalRepository journalRepository;
    private final LedgerRepository ledgerRepository;

    public EntryDto createJournalEntry(JournalEntryRequest request) {
        if (entryRepository.existsById(request.getEntryId())) {
            throw new DuplicateEntryException("Entry with the given ID already exists.");
        }

        var entity = entryMapper.toJournalEntryEntity(request);
        entity.setUserId(SecurityUtils.getCurrentUserId());

        var journal = journalRepository.findById(request.getJournalId())
                .orElseThrow(() -> new JournalNotFoundException("Journal not found"));
        requireJournalOwner(journal);
        entity.setJournal(journal);

        entryRepository.save(entity);
        return entryMapper.toDto(entity);
    }

    public EntryDto createLedgerEntry(LedgerEntryRequest request) {
        if (entryRepository.existsById(request.getEntryId())) {
            throw new DuplicateEntryException("Entry with the given ID already exists.");
        }

        var ledger = ledgerRepository.findById(request.getLedgerId())
                .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));
        requireLedgerOwner(ledger);

        if (LedgerParticular.isInvalidParticular(ledger.getLedgerType(), request.getParticular())) {
            var validParticulars = LedgerParticular.getValidParticulars(ledger.getLedgerType());
            throw new IllegalArgumentException(
                    "Invalid particular '" + request.getParticular() + "' for " + ledger.getLedgerType()
                            + " ledger. Valid particulars: " + validParticulars);
        }

        var entity = entryMapper.toLedgerEntryEntity(request);
        entity.setUserId(SecurityUtils.getCurrentUserId());
        entity.setLedger(ledger);

        entryRepository.save(entity);
        return entryMapper.toDto(entity);
    }

    public EntryDto getEntryById(String entryId) {
        var entity = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntryNotFoundException("Entry not found"));
        requireOwner(entity);
        return entryMapper.toDto(entity);
    }

    public Page<EntryDto> getEntriesByJournal(String journalId, Pageable pageable) {
        var journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal not found"));
        requireJournalOwner(journal);
        return entryRepository.findByJournal(journal, pageable).map(entryMapper::toDto);
    }

    public Page<EntryDto> getEntriesByLedger(String ledgerId, Pageable pageable) {
        var ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));
        requireLedgerOwner(ledger);
        return entryRepository.findByLedger(ledger, pageable).map(entryMapper::toDto);
    }

    public EntryDto updateJournalEntry(String entryId, UpdateJournalEntryRequest request) {
        var entity = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntryNotFoundException("Entry not found"));
        requireOwner(entity);

        if (entity.getJournal() == null) {
            throw new IllegalArgumentException("Cannot update a ledger entry as a journal entry");
        }

        entryMapper.updateJournalEntryEntity(request, entity);
        entryRepository.save(entity);
        return entryMapper.toDto(entity);
    }

    public EntryDto updateLedgerEntry(String entryId, UpdateLedgerEntryRequest request) {
        var entity = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntryNotFoundException("Entry not found"));
        requireOwner(entity);

        var ledger = entity.getLedger();
        if (ledger == null) {
            throw new IllegalArgumentException("Cannot update a journal entry as a ledger entry");
        }

        if (request.getParticular() != null
                && LedgerParticular.isInvalidParticular(ledger.getLedgerType(), request.getParticular())) {
            var validParticulars = LedgerParticular.getValidParticulars(ledger.getLedgerType());
            throw new IllegalArgumentException(
                    "Invalid particular '" + request.getParticular() + "' for " + ledger.getLedgerType()
                            + " ledger. Valid particulars: " + validParticulars);
        }

        entryMapper.updateLedgerEntryEntity(request, entity);
        entryRepository.save(entity);
        return entryMapper.toDto(entity);
    }

    public void deleteEntry(String entryId) {
        var entity = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntryNotFoundException("Entry not found"));
        requireOwner(entity);
        entryRepository.delete(entity);
    }

    private void requireOwner(EntryEntity entity) {
        if (Ownership.denied(entity.getUserId())) {
            throw new EntryNotFoundException("Entry not found");
        }
    }

    private void requireJournalOwner(JournalEntity journal) {
        if (Ownership.denied(journal.getUserId())) {
            throw new JournalNotFoundException("Journal not found");
        }
    }

    private void requireLedgerOwner(LedgerEntity ledger) {
        if (Ownership.denied(ledger.getUserId())) {
            throw new LedgerNotFoundException("Ledger not found");
        }
    }
}
