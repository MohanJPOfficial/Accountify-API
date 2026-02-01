package com.mkdevelopers.accountify.journal.service;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.journal.dto.CreateJournalRequest;
import com.mkdevelopers.accountify.journal.dto.JournalDto;
import com.mkdevelopers.accountify.journal.dto.UpdateJournalRequest;
import com.mkdevelopers.accountify.journal.exception.DuplicateJournalException;
import com.mkdevelopers.accountify.journal.exception.JournalNotFoundException;
import com.mkdevelopers.accountify.journal.mapper.JournalMapper;
import com.mkdevelopers.accountify.journal.repository.JournalRepository;
import com.mkdevelopers.accountify.user.entity.UserEntity;
import com.mkdevelopers.accountify.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class JournalService {

    private static final String CURRENT_USER_ID = "uuid-007";

    private final JournalRepository journalRepository;
    private final JournalMapper journalMapper;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    private UserEntity getCurrentUser() {
        return userRepository.findById(CURRENT_USER_ID)
                .orElseThrow(() -> new IllegalStateException("Current user not found. Add user with id: " + CURRENT_USER_ID));
    }

    private void ensureJournalBelongsToCurrentUser(com.mkdevelopers.accountify.journal.entity.JournalEntity journal) {
        if (journal.getBusiness() == null || !CURRENT_USER_ID.equals(journal.getBusiness().getUser().getUserId())) {
            throw new JournalNotFoundException("Journal with the given ID does not exist.");
        }
    }

    public JournalDto createJournal(CreateJournalRequest request) {
        if (journalRepository.existsById(request.getJournalId())) {
            throw new DuplicateJournalException("Journal with the given ID already exists.");
        }

        BusinessEntity business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new BusinessNotFoundException("Business with the given ID does not exist."));
        if (!CURRENT_USER_ID.equals(business.getUser().getUserId())) {
            throw new BusinessNotFoundException("Business with the given ID does not exist.");
        }

        var user = getCurrentUser();
        var entity = journalMapper.toEntity(request);
        entity.setBusiness(business);
        entity.setUserId(user.getUserId());
        journalRepository.save(entity);

        return journalMapper.toDto(entity);
    }

    public JournalDto getJournalById(String journalId) {
        var entity = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal with the given ID does not exist."));
        ensureJournalBelongsToCurrentUser(entity);
        return journalMapper.toDto(entity);
    }

    public Page<JournalDto> getJournalsByBusiness(String businessId, Pageable pageable) {
        BusinessEntity business = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business with the given ID does not exist."));
        if (!CURRENT_USER_ID.equals(business.getUser().getUserId())) {
            throw new BusinessNotFoundException("Business with the given ID does not exist.");
        }
        return journalRepository.findByBusiness(business, pageable).map(journalMapper::toDto);
    }

    public JournalDto updateJournal(String journalId, UpdateJournalRequest request) {
        var entity = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal with the given ID does not exist."));
        ensureJournalBelongsToCurrentUser(entity);

        journalMapper.updateEntity(request, entity);
        journalRepository.save(entity);

        return journalMapper.toDto(entity);
    }

    public void deleteJournal(String journalId) {
        var entity = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal with the given ID does not exist."));
        ensureJournalBelongsToCurrentUser(entity);
        journalRepository.deleteById(journalId);
    }
}
