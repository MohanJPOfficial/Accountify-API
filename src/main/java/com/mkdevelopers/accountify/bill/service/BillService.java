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

import org.springframework.transaction.annotation.Transactional;
import com.mkdevelopers.accountify.bill.entity.BillEntity;
import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.common.utils.Ownership;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;

@RequiredArgsConstructor
@Service
@Transactional
public class BillService {

    // Removed hardcoded uuid-007

    private final BillRepository billRepository;
    private final BillMapper billMapper;
    private final BusinessRepository businessRepository;
    private final LedgerRepository ledgerRepository;

    public BillDto createBill(CreateBillRequest request) {
        if (billRepository.existsById(request.getBillId())) {
            throw new DuplicateBillException("Bill with the given ID already exists.");
        }

        boolean hasBusiness = request.getBusinessId() != null && !request.getBusinessId().isBlank();
        boolean hasLedger = request.getLedgerId() != null && !request.getLedgerId().isBlank();
        if (hasBusiness == hasLedger) {
            throw new IllegalArgumentException(
                    hasBusiness
                            ? "Provide either businessId or ledgerId, not both"
                            : "Either businessId or ledgerId must be provided");
        }

        var entity = billMapper.toEntity(request);
        entity.setUserId(SecurityUtils.getCurrentUserId());

        if (hasBusiness) {
            var business = businessRepository.findById(request.getBusinessId())
                    .orElseThrow(() -> new BusinessNotFoundException("Business not found"));
            requireBusinessOwner(business);
            entity.setBusiness(business);
        }

        if (hasLedger) {
            var ledger = ledgerRepository.findById(request.getLedgerId())
                    .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));
            requireLedgerOwner(ledger);
            entity.setLedger(ledger);
        }

        billRepository.save(entity);
        return billMapper.toDto(entity);
    }

    public BillDto getBillById(String billId) {
        var entity = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
        requireOwner(entity);
        return billMapper.toDto(entity);
    }

    public Page<BillDto> getBillsByBusiness(String businessId, Pageable pageable) {
        var business = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business not found"));
        requireBusinessOwner(business);
        return billRepository.findByBusiness(business, pageable).map(billMapper::toDto);
    }

    public Page<BillDto> getBillsByLedger(String ledgerId, Pageable pageable) {
        var ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));
        requireLedgerOwner(ledger);
        return billRepository.findByLedger(ledger, pageable).map(billMapper::toDto);
    }

    public BillDto updateBill(String billId, UpdateBillRequest request) {
        var entity = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
        requireOwner(entity);
        billMapper.updateEntity(request, entity);
        billRepository.save(entity);
        return billMapper.toDto(entity);
    }

    public BillDto updateBillTax(String billId, UpdateBillTaxRequest request) {
        var entity = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
        requireOwner(entity);
        billMapper.updateTaxEntity(request, entity);
        billRepository.save(entity);
        return billMapper.toDto(entity);
    }

    public void deleteBill(String billId) {
        var entity = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
        requireOwner(entity);
        billRepository.delete(entity);
    }

    private void requireOwner(BillEntity entity) {
        if (Ownership.denied(entity.getUserId())) {
            throw new BillNotFoundException("Bill not found");
        }
    }

    private void requireBusinessOwner(BusinessEntity business) {
        String ownerId = business.getUser() == null ? null : business.getUser().getUserId();
        if (Ownership.denied(ownerId)) {
            throw new BusinessNotFoundException("Business not found");
        }
    }

    private void requireLedgerOwner(LedgerEntity ledger) {
        if (Ownership.denied(ledger.getUserId())) {
            throw new LedgerNotFoundException("Ledger not found");
        }
    }
}
