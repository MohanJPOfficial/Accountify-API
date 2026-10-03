package com.mkdevelopers.accountify.ledger.service;

import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.ledger.dto.CreateLedgerRequest;
import com.mkdevelopers.accountify.ledger.dto.LedgerDto;
import com.mkdevelopers.accountify.ledger.dto.UpdateLedgerRequest;
import com.mkdevelopers.accountify.ledger.exception.DuplicateLedgerException;
import com.mkdevelopers.accountify.ledger.exception.LedgerNotFoundException;
import com.mkdevelopers.accountify.ledger.mapper.LedgerMapper;
import com.mkdevelopers.accountify.ledger.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.common.utils.Ownership;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;

@RequiredArgsConstructor
@Service
@Transactional
public class LedgerService {

    // Removed hardcoded uuid-007

    private final LedgerRepository ledgerRepository;
    private final LedgerMapper ledgerMapper;
    private final BusinessRepository businessRepository;

    public LedgerDto createLedger(CreateLedgerRequest request) {
        var business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new BusinessNotFoundException("Business not found"));
        requireBusinessOwner(business);

        if (ledgerRepository.existsById(request.getLedgerId())) {
            throw new DuplicateLedgerException("Ledger with the given ID already exists.");
        }

        var entity = ledgerMapper.toEntity(request);
        entity.setUserId(SecurityUtils.getCurrentUserId());
        entity.setBusiness(business);

        ledgerRepository.save(entity);
        return ledgerMapper.toDto(entity);
    }

    public LedgerDto getLedgerById(String ledgerId) {
        var entity = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));
        requireOwner(entity);

        return ledgerMapper.toDto(entity);
    }

    public Page<LedgerDto> getLedgersByBusiness(String businessId, Pageable pageable) {
        var business = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business not found"));
        requireBusinessOwner(business);

        return ledgerRepository.findByBusiness(business, pageable).map(ledgerMapper::toDto);
    }

    public LedgerDto updateLedger(String ledgerId, UpdateLedgerRequest request) {
        var entity = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));
        requireOwner(entity);

        ledgerMapper.updateEntity(request, entity);
        ledgerRepository.save(entity);
        return ledgerMapper.toDto(entity);
    }

    public void deleteLedger(String ledgerId) {
        var entity = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new LedgerNotFoundException("Ledger not found"));
        requireOwner(entity);
        ledgerRepository.delete(entity);
    }

    private void requireOwner(LedgerEntity entity) {
        if (Ownership.denied(entity.getUserId())) {
            throw new LedgerNotFoundException("Ledger not found");
        }
    }

    private void requireBusinessOwner(BusinessEntity business) {
        String ownerId = business.getUser() == null ? null : business.getUser().getUserId();
        if (Ownership.denied(ownerId)) {
            throw new BusinessNotFoundException("Business not found");
        }
    }
}
