package com.mkdevelopers.accountify.bill.service;

import com.mkdevelopers.accountify.bill.dto.BillDto;
import com.mkdevelopers.accountify.bill.dto.CreateBillRequest;
import com.mkdevelopers.accountify.bill.dto.UpdateBillRequest;
import com.mkdevelopers.accountify.bill.dto.UpdateBillTaxRequest;
import com.mkdevelopers.accountify.bill.entity.BillEntity;
import com.mkdevelopers.accountify.bill.exception.BillNotFoundException;
import com.mkdevelopers.accountify.bill.mapper.BillMapper;
import com.mkdevelopers.accountify.bill.repository.BillRepository;
import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import com.mkdevelopers.accountify.ledger.exception.LedgerNotFoundException;
import com.mkdevelopers.accountify.ledger.repository.LedgerRepository;
import com.mkdevelopers.accountify.user.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

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

    private MockedStatic<SecurityUtils> security;

    @BeforeEach
    void setUp() {
        security = mockStatic(SecurityUtils.class);
        security.when(SecurityUtils::getCurrentUserId).thenReturn("user-a");
    }

    @AfterEach
    void tearDown() {
        security.close();
    }

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
        var request = new CreateBillRequest();
        request.setBillId("bill-1");
        request.setBusinessId("  ");
        request.setLedgerId("l1");
        var ledger = new LedgerEntity();
        ledger.setUserId("user-a");
        var entity = new BillEntity();
        var dto = billDto();

        when(billRepository.existsById("bill-1")).thenReturn(false);
        when(ledgerRepository.findById("l1")).thenReturn(Optional.of(ledger));
        when(billMapper.toEntity(request)).thenReturn(entity);
        when(billMapper.toDto(entity)).thenReturn(dto);

        assertEquals(dto, billService.createBill(request));
        verify(businessRepository, never()).findById(any());
        assertEquals(ledger, entity.getLedger());
    }

    @Test
    void getUpdateAndDeleteRejectAnotherUsersBill() {
        var bill = new BillEntity();
        bill.setUserId("user-b");
        when(billRepository.findById("bill-1")).thenReturn(Optional.of(bill));

        assertThrows(BillNotFoundException.class, () -> billService.getBillById("bill-1"));
        assertThrows(BillNotFoundException.class,
                () -> billService.updateBill("bill-1", new UpdateBillRequest()));
        assertThrows(BillNotFoundException.class,
                () -> billService.updateBillTax("bill-1", new UpdateBillTaxRequest()));
        assertThrows(BillNotFoundException.class, () -> billService.deleteBill("bill-1"));
    }

    @Test
    void createAndListRejectAnotherUsersBusiness() {
        var owner = new UserEntity();
        owner.setUserId("user-b");
        var business = new BusinessEntity();
        business.setUser(owner);

        var request = new CreateBillRequest();
        request.setBillId("bill-1");
        request.setBusinessId("b1");

        when(billRepository.existsById("bill-1")).thenReturn(false);
        when(billMapper.toEntity(request)).thenReturn(new BillEntity());
        when(businessRepository.findById("b1")).thenReturn(Optional.of(business));

        assertThrows(BusinessNotFoundException.class, () -> billService.createBill(request));
        assertThrows(BusinessNotFoundException.class,
                () -> billService.getBillsByBusiness("b1", Pageable.unpaged()));
    }

    @Test
    void createAndListRejectAnotherUsersLedger() {
        var ledger = new LedgerEntity();
        ledger.setUserId("user-b");

        var request = new CreateBillRequest();
        request.setBillId("bill-1");
        request.setLedgerId("l1");

        when(billRepository.existsById("bill-1")).thenReturn(false);
        when(billMapper.toEntity(request)).thenReturn(new BillEntity());
        when(ledgerRepository.findById("l1")).thenReturn(Optional.of(ledger));

        assertThrows(LedgerNotFoundException.class, () -> billService.createBill(request));
        assertThrows(LedgerNotFoundException.class,
                () -> billService.getBillsByLedger("l1", Pageable.unpaged()));
    }

    @Test
    void ownerCanGetAndUpdateBill() {
        var bill = new BillEntity();
        bill.setUserId("user-a");
        var dto = billDto();
        when(billRepository.findById("bill-1")).thenReturn(Optional.of(bill));
        when(billMapper.toDto(bill)).thenReturn(dto);

        assertEquals(dto, billService.getBillById("bill-1"));
        assertEquals(dto, billService.updateBill("bill-1", new UpdateBillRequest()));
    }

    private static BillDto billDto() {
        return new BillDto("bill-1", null, "l1", "1", "Invoice", "2026-01-01", null, "City", null, null, "29", 1L);
    }
}
