package com.mkdevelopers.accountify.bill.service;

import com.mkdevelopers.accountify.bill.dto.CreateBillRequest;
import com.mkdevelopers.accountify.bill.mapper.BillMapper;
import com.mkdevelopers.accountify.bill.repository.BillRepository;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.ledger.repository.LedgerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
