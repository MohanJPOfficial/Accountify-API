package com.mkdevelopers.accountify.billentry.controller;

import com.mkdevelopers.accountify.billentry.dto.BillEntryDto;
import com.mkdevelopers.accountify.billentry.dto.CreateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.dto.UpdateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.service.BillEntryService;
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
@RequestMapping("/bill-entries")
class BillEntryController {

    private final BillEntryService billEntryService;

    @PostMapping
    public ResponseEntity<BillEntryDto> createBillEntry(
            @Valid @RequestBody CreateBillEntryRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var dto = billEntryService.createBillEntry(request);
        var uri = uriBuilder.replacePath("/bill-entries/{id}").buildAndExpand(dto.billEntryId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/{id}")
    public BillEntryDto getBillEntryById(@PathVariable String id) {
        return billEntryService.getBillEntryById(id);
    }

    @GetMapping("/bill/{billId}")
    public Page<BillEntryDto> getBillEntriesByBill(
            @PathVariable String billId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return billEntryService.getBillEntriesByBill(billId, pageable);
    }

    @PutMapping("/{id}")
    public BillEntryDto updateBillEntry(
            @PathVariable String id,
            @Valid @RequestBody UpdateBillEntryRequest request
    ) {
        return billEntryService.updateBillEntry(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillEntry(@PathVariable String id) {
        billEntryService.deleteBillEntry(id);
        return ResponseEntity.noContent().build();
    }
}
