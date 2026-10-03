package com.mkdevelopers.accountify.billentry.service;

import com.mkdevelopers.accountify.bill.exception.BillNotFoundException;
import com.mkdevelopers.accountify.bill.repository.BillRepository;
import com.mkdevelopers.accountify.billentry.dto.BillEntryDto;
import com.mkdevelopers.accountify.billentry.dto.CreateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.dto.UpdateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.exception.BillEntryNotFoundException;
import com.mkdevelopers.accountify.billentry.exception.DuplicateBillEntryException;
import com.mkdevelopers.accountify.billentry.mapper.BillEntryMapper;
import com.mkdevelopers.accountify.billentry.repository.BillEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.mkdevelopers.accountify.bill.entity.BillEntity;
import com.mkdevelopers.accountify.billentry.entity.BillEntryEntity;
import com.mkdevelopers.accountify.common.utils.Ownership;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;

@RequiredArgsConstructor
@Service
@Transactional
public class BillEntryService {

    // Removed hardcoded uuid-007

    private final BillEntryRepository billEntryRepository;
    private final BillEntryMapper billEntryMapper;
    private final BillRepository billRepository;

    public BillEntryDto createBillEntry(CreateBillEntryRequest request) {
        if (billEntryRepository.existsById(request.getBillEntryId())) {
            throw new DuplicateBillEntryException("Bill entry with the given ID already exists.");
        }

        var bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
        requireBillOwner(bill);

        var entity = billEntryMapper.toEntity(request);
        entity.setUserId(SecurityUtils.getCurrentUserId());
        entity.setBill(bill);

        billEntryRepository.save(entity);
        return billEntryMapper.toDto(entity);
    }

    public BillEntryDto getBillEntryById(String billEntryId) {
        var entity = billEntryRepository.findById(billEntryId)
                .orElseThrow(() -> new BillEntryNotFoundException("Bill entry not found"));
        requireOwner(entity);
        return billEntryMapper.toDto(entity);
    }

    public Page<BillEntryDto> getBillEntriesByBill(String billId, Pageable pageable) {
        var bill = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
        requireBillOwner(bill);
        return billEntryRepository.findByBill(bill, pageable).map(billEntryMapper::toDto);
    }

    public BillEntryDto updateBillEntry(String billEntryId, UpdateBillEntryRequest request) {
        var entity = billEntryRepository.findById(billEntryId)
                .orElseThrow(() -> new BillEntryNotFoundException("Bill entry not found"));
        requireOwner(entity);
        billEntryMapper.updateEntity(request, entity);
        billEntryRepository.save(entity);
        return billEntryMapper.toDto(entity);
    }

    public void deleteBillEntry(String billEntryId) {
        var entity = billEntryRepository.findById(billEntryId)
                .orElseThrow(() -> new BillEntryNotFoundException("Bill entry not found"));
        requireOwner(entity);
        billEntryRepository.delete(entity);
    }

    private void requireOwner(BillEntryEntity entity) {
        if (Ownership.denied(entity.getUserId())) {
            throw new BillEntryNotFoundException("Bill entry not found");
        }
    }

    private void requireBillOwner(BillEntity bill) {
        if (Ownership.denied(bill.getUserId())) {
            throw new BillNotFoundException("Bill not found");
        }
    }
}
