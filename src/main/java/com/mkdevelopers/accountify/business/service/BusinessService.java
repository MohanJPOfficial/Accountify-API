package com.mkdevelopers.accountify.business.service;

import com.mkdevelopers.accountify.business.dto.BusinessDto;
import com.mkdevelopers.accountify.business.dto.CreateBusinessRequest;
import com.mkdevelopers.accountify.business.dto.UpdateBusinessRequest;
import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.exception.DuplicateBusinessException;
import com.mkdevelopers.accountify.business.mapper.BusinessMapper;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.user.entity.UserEntity;
import com.mkdevelopers.accountify.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BusinessService {

    /**
     * Placeholder until auth is implemented. Ensure this user exists in DB (e.g. via migration or seed).
     */
    private static final String CURRENT_USER_ID = "uuid-007";

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;
    private final UserRepository userRepository;

    private UserEntity getCurrentUser() {
        return userRepository.findById(CURRENT_USER_ID)
                .orElseThrow(() -> new IllegalStateException("Current user not found. Add user with id: " + CURRENT_USER_ID));
    }

    public BusinessDto createBusiness(CreateBusinessRequest businessRequest) {
        if (businessRepository.existsById(businessRequest.getBusinessId())) {
            throw new DuplicateBusinessException("Business with the given ID already exists.");
        }

        var user = getCurrentUser();
        var businessEntity = businessMapper.toEntity(businessRequest);
        businessEntity.setUser(user);
        businessRepository.save(businessEntity);

        return businessMapper.toDto(businessEntity);
    }

    public BusinessDto getBusinessById(String businessId) {
        var entity = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business with the given ID does not exist."));
        return businessMapper.toDto(entity);
    }

    public Page<BusinessDto> getAllBusinesses(Pageable pageable) {
        var user = getCurrentUser();
        return businessRepository.findByUser(user, pageable).map(businessMapper::toDto);
    }

    public BusinessDto updateBusiness(String businessId, UpdateBusinessRequest businessRequest) {
        var businessEntity = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business with the given ID does not exist."));

        businessMapper.updateEntity(businessRequest, businessEntity);
        businessRepository.save(businessEntity);

        return businessMapper.toDto(businessEntity);
    }

    public void deleteBusiness(String businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new BusinessNotFoundException("Business with the given ID does not exist.");
        }
        businessRepository.deleteById(businessId);
    }
}
