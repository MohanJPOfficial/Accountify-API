package com.mkdevelopers.accountify.entry.service;

import com.mkdevelopers.accountify.entry.dto.CreateEntryRequest;
import com.mkdevelopers.accountify.entry.dto.EntryDto;
import com.mkdevelopers.accountify.entry.dto.UpdateEntryRequest;
import com.mkdevelopers.accountify.entry.exception.DuplicateEntryException;
import com.mkdevelopers.accountify.entry.exception.EntryNotFoundException;
import com.mkdevelopers.accountify.entry.mapper.EntryMapper;
import com.mkdevelopers.accountify.entry.repository.EntryRepository;
import com.mkdevelopers.accountify.journal.exception.JournalNotFoundException;
import com.mkdevelopers.accountify.journal.repository.JournalRepository;
import com.mkdevelopers.accountify.ledger.exception.LedgerNotFoundException;
import com.mkdevelopers.accountify.ledger.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EntryService {

    private static final String CURRENT_USER_ID = "uuid-007";

    private final EntryRepository entryRepository;
    private final EntryMapper entryMapper;
    private final JournalRepository journalRepository;
    private final LedgerRepository ledgerRepository;

    public EntryDto createEntry(CreateEntryRequest request) {
        if (entryRepository.existsById(request.getEntryId())) {
            throw new DuplicateEntryException("Entry with the given ID already exists.");
        }

        if (request.getJournalId() != null && request.getLedgerId() != null) {
            throw new IllegalArgumentException("Entry cannot belong to both Journal and Ledger at the same time.");
        }
        if (request.getJournalId() == null && request.getLedgerId() == null) {
            throw new IllegalArgumentException("Entry must belong to either Journal or Ledger.");
        }

        var entity = entryMapper.toEntity(request);
        entity.setUserId(CURRENT_USER_ID);

        if (request.getJournalId() != null) {
            var journal = journalRepository.findById(request.getJournalId())
                    .orElseThrow(() -> new JournalNotFoundException("Journal not found"));
            if (!CURRENT_USER_ID.equals(journal.getUserId())) {
                throw new JournalNotFoundException("Journal not found");
            }
            entity.setJournal(journal);
        } else {
            var ledger = ledgerRepository.findById(request.getLedgerId())
                    .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));
            if (!CURRENT_USER_ID.equals(ledger.getUserId())) {
                throw new LedgerNotFoundException("Ledger not found");
            }
            entity.setLedger(ledger);
        }
        
        entryRepository.save(entity);
        return entryMapper.toDto(entity);
    }

    public EntryDto getEntryById(String entryId) {
        var entity = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntryNotFoundException("Entry not found"));
        
        if (!CURRENT_USER_ID.equals(entity.getUserId())) {
             throw new EntryNotFoundException("Entry not found");
        }
        
        return entryMapper.toDto(entity);
    }

    public Page<EntryDto> getEntriesByJournal(String journalId, Pageable pageable) {
        var journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal not found"));

        if (!CURRENT_USER_ID.equals(journal.getUserId())) {
             throw new JournalNotFoundException("Journal not found");
        }

        return entryRepository.findByJournal(journal, pageable).map(entryMapper::toDto);
    }

    public Page<EntryDto> getEntriesByLedger(String ledgerId, Pageable pageable) {
        var ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));

        if (!CURRENT_USER_ID.equals(ledger.getUserId())) {
             throw new LedgerNotFoundException("Ledger not found");
        }

        return entryRepository.findByLedger(ledger, pageable).map(entryMapper::toDto);
    }

    public EntryDto updateEntry(String entryId, UpdateEntryRequest request) {
        var entity = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntryNotFoundException("Entry not found"));

        if (!CURRENT_USER_ID.equals(entity.getUserId())) {
             throw new EntryNotFoundException("Entry not found");
        }

        entryMapper.updateEntity(request, entity);
        entryRepository.save(entity);
        return entryMapper.toDto(entity);
    }

    public void deleteEntry(String entryId) {
        var entity = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntryNotFoundException("Entry not found"));

        if (!CURRENT_USER_ID.equals(entity.getUserId())) {
             throw new EntryNotFoundException("Entry not found");
        }

        entryRepository.deleteById(entryId);
    }
}
