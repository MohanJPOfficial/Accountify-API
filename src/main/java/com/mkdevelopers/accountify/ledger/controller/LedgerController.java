package com.mkdevelopers.accountify.ledger.controller;

import com.mkdevelopers.accountify.ledger.dto.CreateLedgerRequest;
import com.mkdevelopers.accountify.ledger.dto.LedgerDto;
import com.mkdevelopers.accountify.ledger.dto.UpdateLedgerRequest;
import com.mkdevelopers.accountify.ledger.service.LedgerService;
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
@RequestMapping("/ledgers")
class LedgerController {

    private final LedgerService ledgerService;

    @GetMapping(params = "businessId")
    public Page<LedgerDto> getLedgersByBusiness(
            @RequestParam String businessId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ledgerService.getLedgersByBusiness(businessId, pageable);
    }

    @PostMapping
    public ResponseEntity<LedgerDto> createLedger(
            @Valid @RequestBody CreateLedgerRequest request,
            UriComponentsBuilder uriBuilder) {
        var dto = ledgerService.createLedger(request);
        var uri = uriBuilder.path("/ledgers/{id}").buildAndExpand(dto.ledgerId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/{id}")
    public LedgerDto getLedgerById(@PathVariable String id) {
        return ledgerService.getLedgerById(id);
    }

    @PutMapping("/{id}")
    public LedgerDto updateLedger(
            @PathVariable String id,
            @Valid @RequestBody UpdateLedgerRequest request) {
        return ledgerService.updateLedger(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLedger(@PathVariable String id) {
        ledgerService.deleteLedger(id);
        return ResponseEntity.noContent().build();
    }
}
