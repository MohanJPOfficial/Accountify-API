package com.mkdevelopers.accountify.bill.controller;

import com.mkdevelopers.accountify.bill.dto.BillDto;
import com.mkdevelopers.accountify.bill.dto.CreateBillRequest;
import com.mkdevelopers.accountify.bill.dto.UpdateBillRequest;
import com.mkdevelopers.accountify.bill.dto.UpdateBillTaxRequest;
import com.mkdevelopers.accountify.bill.service.BillService;
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
@RequestMapping("/bills")
class BillController {

    private final BillService billService;

    @GetMapping("/business/{businessId}")
    public Page<BillDto> getBillsByBusiness(
            @PathVariable String businessId,
            @PageableDefault(size = 20) Pageable pageable) {
        return billService.getBillsByBusiness(businessId, pageable);
    }

    @GetMapping("/ledger/{ledgerId}")
    public Page<BillDto> getBillsByLedger(
            @PathVariable String ledgerId,
            @PageableDefault(size = 20) Pageable pageable) {
        return billService.getBillsByLedger(ledgerId, pageable);
    }

    @PostMapping
    public ResponseEntity<BillDto> createBill(
            @Valid @RequestBody CreateBillRequest request,
            UriComponentsBuilder uriBuilder) {
        var dto = billService.createBill(request);
        var uri = uriBuilder.replacePath("/bills/{id}").buildAndExpand(dto.billId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/{id}")
    public BillDto getBillById(@PathVariable String id) {
        return billService.getBillById(id);
    }

    @PutMapping("/{id}")
    public BillDto updateBill(
            @PathVariable String id,
            @Valid @RequestBody UpdateBillRequest request) {
        return billService.updateBill(id, request);
    }

    @PutMapping("/{id}/tax")
    public BillDto updateBillTax(
            @PathVariable String id,
            @Valid @RequestBody UpdateBillTaxRequest request) {
        return billService.updateBillTax(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable String id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
