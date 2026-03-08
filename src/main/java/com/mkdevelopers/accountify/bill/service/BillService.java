package com.mkdevelopers.accountify.bill.service;

import com.mkdevelopers.accountify.bill.dto.BillDto;
import com.mkdevelopers.accountify.bill.dto.CreateBillRequest;
import com.mkdevelopers.accountify.bill.dto.UpdateBillRequest;
import com.mkdevelopers.accountify.bill.dto.UpdateBillTaxRequest;
import com.mkdevelopers.accountify.bill.exception.BillNotFoundException;
import com.mkdevelopers.accountify.bill.exception.DuplicateBillException;
import com.mkdevelopers.accountify.bill.mapper.BillMapper;
import com.mkdevelopers.accountify.bill.repository.BillRepository;
import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.ledger.exception.LedgerNotFoundException;
import com.mkdevelopers.accountify.ledger.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BillService {

    private static final String CURRENT_USER_ID = "uuid-007";

    private final BillRepository billRepository;
    private final BillMapper billMapper;
    private final BusinessRepository businessRepository;
    private final LedgerRepository ledgerRepository;

    public BillDto createBill(CreateBillRequest request) {
        if (billRepository.existsById(request.getBillId())) {
            throw new DuplicateBillException("Bill with the given ID already exists.");
        }

        if (request.getBusinessId() == null && request.getLedgerId() == null) {
            throw new IllegalArgumentException("Either businessId or ledgerId must be provided");
        }

        var entity = billMapper.toEntity(request);
        entity.setUserId(CURRENT_USER_ID);

        if (request.getBusinessId() != null) {
            var business = businessRepository.findById(request.getBusinessId())
                    .orElseThrow(() -> new BusinessNotFoundException("Business not found"));
            entity.setBusiness(business);
        }

        if (request.getLedgerId() != null) {
            var ledger = ledgerRepository.findById(request.getLedgerId())
                    .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));
            entity.setLedger(ledger);
        }

        billRepository.save(entity);
        return billMapper.toDto(entity);
    }

    public BillDto getBillById(String billId) {
        var entity = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
        return billMapper.toDto(entity);
    }

    public Page<BillDto> getBillsByBusiness(String businessId, Pageable pageable) {
        var business = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business not found"));
        return billRepository.findByBusiness(business, pageable).map(billMapper::toDto);
    }

    public Page<BillDto> getBillsByLedger(String ledgerId, Pageable pageable) {
        var ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));
        return billRepository.findByLedger(ledger, pageable).map(billMapper::toDto);
    }

    public BillDto updateBill(String billId, UpdateBillRequest request) {
        var entity = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
        billMapper.updateEntity(request, entity);
        billRepository.save(entity);
        return billMapper.toDto(entity);
    }

    public BillDto updateBillTax(String billId, UpdateBillTaxRequest request) {
        var entity = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
        billMapper.updateTaxEntity(request, entity);
        billRepository.save(entity);
        return billMapper.toDto(entity);
    }

    public void deleteBill(String billId) {
        var entity = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
        billRepository.delete(entity);
    }
}
