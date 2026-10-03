package com.mkdevelopers.accountify.journal.service;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.journal.dto.CreateJournalRequest;
import com.mkdevelopers.accountify.journal.dto.UpdateJournalRequest;
import com.mkdevelopers.accountify.journal.entity.JournalEntity;
import com.mkdevelopers.accountify.journal.exception.JournalNotFoundException;
import com.mkdevelopers.accountify.journal.mapper.JournalMapper;
import com.mkdevelopers.accountify.journal.repository.JournalRepository;
import com.mkdevelopers.accountify.user.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JournalServiceTest {

    @Mock
    private JournalRepository journalRepository;
    @Mock
    private JournalMapper journalMapper;
    @Mock
    private BusinessRepository businessRepository;

    private MockedStatic<SecurityUtils> security;
    private JournalService journalService;

    @BeforeEach
    void setUp() {
        security = mockStatic(SecurityUtils.class);
        security.when(SecurityUtils::getCurrentUserId).thenReturn("user-a");
        journalService = new JournalService(journalRepository, journalMapper, businessRepository);
    }

    @AfterEach
    void tearDown() {
        security.close();
    }

    @Test
    void getAndUpdateRejectAnotherUsersJournal() {
        var journal = new JournalEntity();
        journal.setUserId("user-b");
        when(journalRepository.findById("j1")).thenReturn(Optional.of(journal));

        assertThrows(JournalNotFoundException.class, () -> journalService.getJournalById("j1"));
        assertThrows(JournalNotFoundException.class,
                () -> journalService.updateJournal("j1", new UpdateJournalRequest()));
    }

    @Test
    void createRejectsAnotherUsersBusiness() {
        var owner = new UserEntity();
        owner.setUserId("user-b");
        var business = new BusinessEntity();
        business.setUser(owner);

        var request = new CreateJournalRequest();
        request.setJournalId("j1");
        request.setBusinessId("b1");

        when(journalRepository.existsById("j1")).thenReturn(false);
        when(businessRepository.findById("b1")).thenReturn(Optional.of(business));

        assertThrows(BusinessNotFoundException.class, () -> journalService.createJournal(request));
    }
}
