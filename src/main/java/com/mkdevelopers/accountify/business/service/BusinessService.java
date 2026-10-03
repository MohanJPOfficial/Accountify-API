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
import com.mkdevelopers.accountify.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.common.utils.Ownership;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;

@AllArgsConstructor
@Service
@Transactional
public class BusinessService {

    /**
     * Placeholder until auth is implemented. Ensure this user exists in DB (e.g.
     * via migration or seed).
     */
    // Removed hardcoded uuid-007

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    private UserEntity getCurrentUser() {
        return userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new IllegalStateException(
                        "Current user not found. Add user with id: " + SecurityUtils.getCurrentUserId()));
    }

    public BusinessDto createBusiness(CreateBusinessRequest businessRequest) {
        if (businessRepository.existsById(businessRequest.getBusinessId())) {
            throw new DuplicateBusinessException("Business with the given ID already exists.");
        }

        var user = userService.ensureCurrentUser();
        var businessEntity = businessMapper.toEntity(businessRequest);
        businessEntity.setUser(user);
        businessRepository.save(businessEntity);

        return businessMapper.toDto(businessEntity);
    }

    public BusinessDto getBusinessById(String businessId) {
        var entity = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business with the given ID does not exist."));
        requireOwner(entity);
        return businessMapper.toDto(entity);
    }

    public Page<BusinessDto> getAllBusinesses(Pageable pageable) {
        var user = getCurrentUser();
        return businessRepository.findByUser(user, pageable).map(businessMapper::toDto);
    }

    public BusinessDto updateBusiness(String businessId, UpdateBusinessRequest businessRequest) {
        var businessEntity = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business with the given ID does not exist."));
        requireOwner(businessEntity);

        businessMapper.updateEntity(businessRequest, businessEntity);
        businessRepository.save(businessEntity);

        return businessMapper.toDto(businessEntity);
    }

    public void deleteBusiness(String businessId) {
        var businessEntity = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business with the given ID does not exist."));
        requireOwner(businessEntity);
        businessRepository.delete(businessEntity);
    }

    private void requireOwner(BusinessEntity entity) {
        String ownerId = entity.getUser() == null ? null : entity.getUser().getUserId();
        if (Ownership.denied(ownerId)) {
            throw new BusinessNotFoundException("Business with the given ID does not exist.");
        }
    }
}
