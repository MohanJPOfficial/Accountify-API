package com.mkdevelopers.accountify.entry.controller;

import com.mkdevelopers.accountify.entry.dto.EntryDto;
import com.mkdevelopers.accountify.entry.dto.JournalEntryRequest;
import com.mkdevelopers.accountify.entry.dto.LedgerEntryRequest;
import com.mkdevelopers.accountify.entry.dto.UpdateJournalEntryRequest;
import com.mkdevelopers.accountify.entry.dto.UpdateLedgerEntryRequest;
import com.mkdevelopers.accountify.entry.service.EntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/entries")
class EntryController {

    private final EntryService entryService;

    @GetMapping("/journal/{journalId}")
    public Page<EntryDto> getEntriesByJournal(
            @PathVariable String journalId,
            @PageableDefault(size = 20) Pageable pageable) {
        return entryService.getEntriesByJournal(journalId, pageable);
    }

    @GetMapping("/ledger/{ledgerId}")
    public Page<EntryDto> getEntriesByLedger(
            @PathVariable String ledgerId,
            @PageableDefault(size = 20) Pageable pageable) {
        return entryService.getEntriesByLedger(ledgerId, pageable);
    }

    @PostMapping("/journal")
    public ResponseEntity<EntryDto> createJournalEntry(
            @Valid @RequestBody JournalEntryRequest request,
            UriComponentsBuilder uriBuilder) {
        var dto = entryService.createJournalEntry(request);
        var uri = uriBuilder.replacePath("/entries/journal/{id}").buildAndExpand(dto.entryId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PostMapping("/ledger")
    public ResponseEntity<EntryDto> createLedgerEntry(
            @Valid @RequestBody LedgerEntryRequest request,
            UriComponentsBuilder uriBuilder) {
        var dto = entryService.createLedgerEntry(request);
        var uri = uriBuilder.replacePath("/entries/ledger/{id}").buildAndExpand(dto.entryId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/{id}")
    public EntryDto getEntryById(@PathVariable String id) {
        return entryService.getEntryById(id);
    }

    @PutMapping("/journal/{id}")
    public EntryDto updateJournalEntry(
            @PathVariable String id,
            @Valid @RequestBody UpdateJournalEntryRequest request) {
        return entryService.updateJournalEntry(id, request);
    }

    @PutMapping("/ledger/{id}")
    public EntryDto updateLedgerEntry(
            @PathVariable String id,
            @Valid @RequestBody UpdateLedgerEntryRequest request) {
        return entryService.updateLedgerEntry(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String id) {
        entryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
