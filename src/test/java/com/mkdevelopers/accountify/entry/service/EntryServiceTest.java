package com.mkdevelopers.accountify.entry.service;

import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.entry.dto.EntryDto;
import com.mkdevelopers.accountify.entry.dto.JournalEntryRequest;
import com.mkdevelopers.accountify.entry.dto.LedgerEntryRequest;
import com.mkdevelopers.accountify.entry.dto.UpdateJournalEntryRequest;
import com.mkdevelopers.accountify.entry.dto.UpdateLedgerEntryRequest;
import com.mkdevelopers.accountify.entry.entity.EntryEntity;
import com.mkdevelopers.accountify.entry.exception.EntryNotFoundException;
import com.mkdevelopers.accountify.entry.mapper.EntryMapper;
import com.mkdevelopers.accountify.entry.repository.EntryRepository;
import com.mkdevelopers.accountify.journal.entity.JournalEntity;
import com.mkdevelopers.accountify.journal.exception.JournalNotFoundException;
import com.mkdevelopers.accountify.journal.repository.JournalRepository;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import com.mkdevelopers.accountify.ledger.exception.LedgerNotFoundException;
import com.mkdevelopers.accountify.ledger.repository.LedgerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntryServiceTest {

    @Mock
    private EntryRepository entryRepository;
    @Mock
    private EntryMapper entryMapper;
    @Mock
    private JournalRepository journalRepository;
    @Mock
    private LedgerRepository ledgerRepository;

    private MockedStatic<SecurityUtils> security;
    private EntryService entryService;

    @BeforeEach
    void setUp() {
        security = mockStatic(SecurityUtils.class);
        security.when(SecurityUtils::getCurrentUserId).thenReturn("user-a");
        entryService = new EntryService(entryRepository, entryMapper, journalRepository, ledgerRepository);
    }

    @AfterEach
    void tearDown() {
        security.close();
    }

    @Test
    void getUpdateAndDeleteRejectAnotherUsersEntry() {
        var entry = new EntryEntity();
        entry.setUserId("user-b");
        when(entryRepository.findById("e1")).thenReturn(Optional.of(entry));

        assertThrows(EntryNotFoundException.class, () -> entryService.getEntryById("e1"));
        assertThrows(EntryNotFoundException.class, () -> entryService.deleteEntry("e1"));
        assertThrows(EntryNotFoundException.class,
                () -> entryService.updateJournalEntry("e1", new UpdateJournalEntryRequest()));
        assertThrows(EntryNotFoundException.class,
                () -> entryService.updateLedgerEntry("e1", new UpdateLedgerEntryRequest()));
    }

    @Test
    void createAndListRejectAnotherUsersJournal() {
        var journal = new JournalEntity();
        journal.setUserId("user-b");

        var request = new JournalEntryRequest();
        request.setEntryId("e1");
        request.setJournalId("j1");

        when(entryRepository.existsById("e1")).thenReturn(false);
        when(entryMapper.toJournalEntryEntity(request)).thenReturn(new EntryEntity());
        when(journalRepository.findById("j1")).thenReturn(Optional.of(journal));

        assertThrows(JournalNotFoundException.class, () -> entryService.createJournalEntry(request));
        assertThrows(JournalNotFoundException.class,
                () -> entryService.getEntriesByJournal("j1", Pageable.unpaged()));
    }

    @Test
    void createAndListRejectAnotherUsersLedger() {
        var ledger = new LedgerEntity();
        ledger.setUserId("user-b");

        var request = new LedgerEntryRequest();
        request.setEntryId("e1");
        request.setLedgerId("l1");

        when(entryRepository.existsById("e1")).thenReturn(false);
        when(ledgerRepository.findById("l1")).thenReturn(Optional.of(ledger));

        assertThrows(LedgerNotFoundException.class, () -> entryService.createLedgerEntry(request));
        assertThrows(LedgerNotFoundException.class,
                () -> entryService.getEntriesByLedger("l1", Pageable.unpaged()));
    }

    @Test
    void ownerCanGetEntry() {
        var entry = new EntryEntity();
        entry.setUserId("user-a");
        var dto = new EntryDto("e1", "j1", null, "2026-10-03", "Cash", "IN", 10L, 1L);
        when(entryRepository.findById("e1")).thenReturn(Optional.of(entry));
        when(entryMapper.toDto(entry)).thenReturn(dto);

        assertEquals(dto, entryService.getEntryById("e1"));
    }
}
