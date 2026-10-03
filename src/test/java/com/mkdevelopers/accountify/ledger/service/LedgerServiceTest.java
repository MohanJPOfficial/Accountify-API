package com.mkdevelopers.accountify.ledger.service;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.ledger.dto.CreateLedgerRequest;
import com.mkdevelopers.accountify.ledger.dto.LedgerDto;
import com.mkdevelopers.accountify.ledger.dto.UpdateLedgerRequest;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import com.mkdevelopers.accountify.ledger.enums.LedgerType;
import com.mkdevelopers.accountify.ledger.exception.LedgerNotFoundException;
import com.mkdevelopers.accountify.ledger.mapper.LedgerMapper;
import com.mkdevelopers.accountify.ledger.repository.LedgerRepository;
import com.mkdevelopers.accountify.user.entity.UserEntity;
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
class LedgerServiceTest {

    @Mock
    private LedgerRepository ledgerRepository;
    @Mock
    private LedgerMapper ledgerMapper;
    @Mock
    private BusinessRepository businessRepository;

    private MockedStatic<SecurityUtils> security;
    private LedgerService ledgerService;

    @BeforeEach
    void setUp() {
        security = mockStatic(SecurityUtils.class);
        security.when(SecurityUtils::getCurrentUserId).thenReturn("user-a");
        ledgerService = new LedgerService(ledgerRepository, ledgerMapper, businessRepository);
    }

    @AfterEach
    void tearDown() {
        security.close();
    }

    @Test
    void getUpdateAndDeleteRejectAnotherUsersLedger() {
        var ledger = new LedgerEntity();
        ledger.setUserId("user-b");
        when(ledgerRepository.findById("l1")).thenReturn(Optional.of(ledger));

        assertThrows(LedgerNotFoundException.class, () -> ledgerService.getLedgerById("l1"));
        assertThrows(LedgerNotFoundException.class,
                () -> ledgerService.updateLedger("l1", new UpdateLedgerRequest()));
        assertThrows(LedgerNotFoundException.class, () -> ledgerService.deleteLedger("l1"));
    }

    @Test
    void createAndListRejectAnotherUsersBusiness() {
        var owner = new UserEntity();
        owner.setUserId("user-b");
        var business = new BusinessEntity();
        business.setUser(owner);

        var request = new CreateLedgerRequest();
        request.setLedgerId("l1");
        request.setBusinessId("b1");

        when(businessRepository.findById("b1")).thenReturn(Optional.of(business));

        assertThrows(BusinessNotFoundException.class, () -> ledgerService.createLedger(request));
        assertThrows(BusinessNotFoundException.class,
                () -> ledgerService.getLedgersByBusiness("b1", Pageable.unpaged()));
    }

    @Test
    void ownerCanGetAndUpdateLedger() {
        var ledger = new LedgerEntity();
        ledger.setUserId("user-a");
        var dto = new LedgerDto("l1", "b1", LedgerType.SALES, "Shop", null, "Chennai", 1L);
        when(ledgerRepository.findById("l1")).thenReturn(Optional.of(ledger));
        when(ledgerMapper.toDto(ledger)).thenReturn(dto);

        assertEquals(dto, ledgerService.getLedgerById("l1"));
        assertEquals(dto, ledgerService.updateLedger("l1", new UpdateLedgerRequest()));
    }
}
