package com.mkdevelopers.accountify.billentry.service;

import com.mkdevelopers.accountify.bill.entity.BillEntity;
import com.mkdevelopers.accountify.bill.exception.BillNotFoundException;
import com.mkdevelopers.accountify.bill.repository.BillRepository;
import com.mkdevelopers.accountify.billentry.constant.EntryType;
import com.mkdevelopers.accountify.billentry.dto.BillEntryDto;
import com.mkdevelopers.accountify.billentry.dto.CreateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.dto.UpdateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.entity.BillEntryEntity;
import com.mkdevelopers.accountify.billentry.exception.BillEntryNotFoundException;
import com.mkdevelopers.accountify.billentry.mapper.BillEntryMapper;
import com.mkdevelopers.accountify.billentry.repository.BillEntryRepository;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillEntryServiceTest {

    @Mock
    private BillEntryRepository billEntryRepository;
    @Mock
    private BillEntryMapper billEntryMapper;
    @Mock
    private BillRepository billRepository;

    private MockedStatic<SecurityUtils> security;
    private BillEntryService billEntryService;

    @BeforeEach
    void setUp() {
        security = mockStatic(SecurityUtils.class);
        security.when(SecurityUtils::getCurrentUserId).thenReturn("user-a");
        billEntryService = new BillEntryService(billEntryRepository, billEntryMapper, billRepository);
    }

    @AfterEach
    void tearDown() {
        security.close();
    }

    @Test
    void getUpdateAndDeleteRejectAnotherUsersBillEntry() {
        var billEntry = new BillEntryEntity();
        billEntry.setUserId("user-b");
        when(billEntryRepository.findById("be1")).thenReturn(Optional.of(billEntry));

        assertThrows(BillEntryNotFoundException.class, () -> billEntryService.getBillEntryById("be1"));
        assertThrows(BillEntryNotFoundException.class,
                () -> billEntryService.updateBillEntry("be1", new UpdateBillEntryRequest()));
        assertThrows(BillEntryNotFoundException.class, () -> billEntryService.deleteBillEntry("be1"));
    }

    @Test
    void createAndListRejectAnotherUsersBill() {
        var bill = new BillEntity();
        bill.setUserId("user-b");

        var request = new CreateBillEntryRequest();
        request.setBillEntryId("be1");
        request.setBillId("bill1");

        when(billEntryRepository.existsById("be1")).thenReturn(false);
        when(billRepository.findById("bill1")).thenReturn(Optional.of(bill));

        assertThrows(BillNotFoundException.class, () -> billEntryService.createBillEntry(request));
        assertThrows(BillNotFoundException.class,
                () -> billEntryService.getBillEntriesByBill("bill1", Pageable.unpaged()));
    }

    @Test
    void ownerCanGetAndUpdateBillEntry() {
        var billEntry = new BillEntryEntity();
        billEntry.setUserId("user-a");
        var dto = new BillEntryDto("be1", "bill1", "Rice", 100L, 1, EntryType.SALES, null, 1L);
        when(billEntryRepository.findById("be1")).thenReturn(Optional.of(billEntry));
        when(billEntryMapper.toDto(billEntry)).thenReturn(dto);

        assertEquals(dto, billEntryService.getBillEntryById("be1"));
        assertEquals(dto, billEntryService.updateBillEntry("be1", new UpdateBillEntryRequest()));
    }
}
