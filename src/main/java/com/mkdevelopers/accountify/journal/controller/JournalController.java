package com.mkdevelopers.accountify.journal.controller;

import com.mkdevelopers.accountify.journal.dto.CreateJournalRequest;
import com.mkdevelopers.accountify.journal.dto.JournalDto;
import com.mkdevelopers.accountify.journal.dto.UpdateJournalRequest;
import com.mkdevelopers.accountify.journal.service.JournalService;
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
@RequestMapping("/journals")
class JournalController {

    private final JournalService journalService;

    @GetMapping(params = "businessId")
    public Page<JournalDto> getJournalsByBusiness(
            @RequestParam String businessId,
            @PageableDefault(size = 20) Pageable pageable) {
        return journalService.getJournalsByBusiness(businessId, pageable);
    }

    @PostMapping
    public ResponseEntity<JournalDto> createJournal(
            @Valid @RequestBody CreateJournalRequest request,
            UriComponentsBuilder uriBuilder) {
        var dto = journalService.createJournal(request);
        var uri = uriBuilder.path("/journals/{id}").buildAndExpand(dto.journalId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/{id}")
    public JournalDto getJournalById(@PathVariable String id) {
        return journalService.getJournalById(id);
    }

    @PutMapping("/{id}")
    public JournalDto updateJournal(
            @PathVariable String id,
            @Valid @RequestBody UpdateJournalRequest request) {
        return journalService.updateJournal(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJournal(@PathVariable String id) {
        journalService.deleteJournal(id);
        return ResponseEntity.noContent().build();
    }
}
