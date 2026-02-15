package com.mkdevelopers.accountify.journal.service;

import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.journal.dto.CreateJournalRequest;
import com.mkdevelopers.accountify.journal.dto.JournalDto;
import com.mkdevelopers.accountify.journal.dto.UpdateJournalRequest;
import com.mkdevelopers.accountify.journal.exception.DuplicateJournalException;
import com.mkdevelopers.accountify.journal.exception.JournalNotFoundException;
import com.mkdevelopers.accountify.journal.mapper.JournalMapper;
import com.mkdevelopers.accountify.journal.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JournalService {

    private static final String CURRENT_USER_ID = "uuid-007";

    private final JournalRepository journalRepository;
    private final JournalMapper journalMapper;
    private final BusinessRepository businessRepository;

    public JournalDto createJournal(CreateJournalRequest request) {
        if (journalRepository.existsById(request.getJournalId())) {
            throw new DuplicateJournalException("Journal with the given ID already exists.");
        }

        var business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new BusinessNotFoundException("Business with the given ID does not exist."));

        var entity = journalMapper.toEntity(request);
        entity.setBusiness(business);
        entity.setUserId(CURRENT_USER_ID);
        journalRepository.save(entity);

        return journalMapper.toDto(entity);
    }

    public JournalDto getJournalById(String journalId) {
        var entity = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal with the given ID does not exist."));
        return journalMapper.toDto(entity);
    }

    public Page<JournalDto> getJournalsByBusiness(String businessId, Pageable pageable) {
        var business = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business with the given ID does not exist."));
        return journalRepository.findByBusiness(business, pageable).map(journalMapper::toDto);
    }

    public JournalDto updateJournal(String journalId, UpdateJournalRequest request) {
        var entity = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal with the given ID does not exist."));
        journalMapper.updateEntity(request, entity);
        journalRepository.save(entity);

        return journalMapper.toDto(entity);
    }

    public void deleteJournal(String journalId) {
        var entity = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal with the given ID does not exist."));
        journalRepository.delete(entity);
    }
}
