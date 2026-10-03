package com.mkdevelopers.accountify.bill.service;

import com.mkdevelopers.accountify.bill.dto.BillDto;
import com.mkdevelopers.accountify.bill.dto.CreateBillRequest;
import com.mkdevelopers.accountify.bill.entity.BillEntity;
import com.mkdevelopers.accountify.bill.mapper.BillMapper;
import com.mkdevelopers.accountify.bill.repository.BillRepository;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import com.mkdevelopers.accountify.ledger.repository.LedgerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private BillRepository billRepository;
    @Mock
    private BillMapper billMapper;
    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private LedgerRepository ledgerRepository;

    @InjectMocks
    private BillService billService;

    @Test
    void createRejectsBothParents() {
        var request = new CreateBillRequest();
        request.setBillId("bill-1");
        request.setBusinessId("b1");
        request.setLedgerId("l1");
        when(billRepository.existsById("bill-1")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> billService.createBill(request));
    }

    @Test
    void blankBusinessIdIsTreatedAsLedgerOnly() {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUserId).thenReturn("user-a");

            var request = new CreateBillRequest();
            request.setBillId("bill-1");
            request.setBusinessId("  ");
            request.setLedgerId("l1");
            var ledger = new LedgerEntity();
            ledger.setUserId("user-a");
            var entity = new BillEntity();
            var dto = new BillDto("bill-1", null, "l1", "1", "Invoice", "2026-01-01", null, "City", null, null, "29", 1L);

            when(billRepository.existsById("bill-1")).thenReturn(false);
            when(ledgerRepository.findById("l1")).thenReturn(Optional.of(ledger));
            when(billMapper.toEntity(request)).thenReturn(entity);
            when(billMapper.toDto(entity)).thenReturn(dto);

            assertEquals(dto, billService.createBill(request));
            verify(businessRepository, never()).findById(any());
            assertEquals(ledger, entity.getLedger());
        }
    }
}
