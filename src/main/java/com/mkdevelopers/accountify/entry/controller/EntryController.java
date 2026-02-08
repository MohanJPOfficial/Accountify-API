package com.mkdevelopers.accountify.entry.controller;

import com.mkdevelopers.accountify.entry.dto.CreateEntryRequest;
import com.mkdevelopers.accountify.entry.dto.EntryDto;
import com.mkdevelopers.accountify.entry.dto.UpdateEntryRequest;
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

    @GetMapping(params = "journalId")
    public Page<EntryDto> getEntriesByJournal(
            @RequestParam String journalId,
            @PageableDefault(size = 20) Pageable pageable) {
        return entryService.getEntriesByJournal(journalId, pageable);
    }

    @GetMapping(params = "ledgerId")
    public Page<EntryDto> getEntriesByLedger(
            @RequestParam String ledgerId,
            @PageableDefault(size = 20) Pageable pageable) {
        return entryService.getEntriesByLedger(ledgerId, pageable);
    }

    @PostMapping
    public ResponseEntity<EntryDto> createEntry(
            @Valid @RequestBody CreateEntryRequest request,
            UriComponentsBuilder uriBuilder) {
        var dto = entryService.createEntry(request);
        var uri = uriBuilder.path("/entries/{id}").buildAndExpand(dto.entryId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/{id}")
    public EntryDto getEntryById(@PathVariable String id) {
        return entryService.getEntryById(id);
    }

    @PutMapping("/{id}")
    public EntryDto updateEntry(
            @PathVariable String id,
            @Valid @RequestBody UpdateEntryRequest request) {
        return entryService.updateEntry(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String id) {
        entryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
