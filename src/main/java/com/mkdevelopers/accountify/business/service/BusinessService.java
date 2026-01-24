package com.mkdevelopers.accountify.business.service;

import com.mkdevelopers.accountify.business.dto.BusinessDto;
import com.mkdevelopers.accountify.business.dto.CreateBusinessRequest;
import com.mkdevelopers.accountify.business.dto.UpdateBusinessRequest;
import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.exception.DuplicateBusinessException;
import com.mkdevelopers.accountify.business.mapper.BusinessMapper;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    public BusinessDto createBusiness(CreateBusinessRequest businessRequest) {

        if(businessRepository.existsById(businessRequest.getBusinessId())){
            throw new DuplicateBusinessException("Business with the given ID already exists.");
        }

        var user = new UserEntity(
                "uuid-007",
                "Mohan JP",
                "jpmohan@gmailcom",
                Collections.emptySet()
        );

        var businessEntity = businessMapper.toEntity(businessRequest);
        businessEntity.setUser(user);
        businessRepository.save(businessEntity);

        return businessMapper.toDto(businessEntity);
    }

    public List<BusinessDto> getAllBusinesses() {

        var user = new UserEntity(
                "uuid-007",
                "Mohan JP",
                "jpmohan@gmailcom",
                Collections.emptySet()
        );

        var businessEntities = businessRepository.getAllBusinessesByUser(user);
        return businessEntities.stream()
                .map(businessMapper::toDto)
                .toList();
    }

    public BusinessDto updateBusiness(String businessId, UpdateBusinessRequest businessRequest) {
        var businessEntity = businessRepository.findById(businessId).orElse(null);

        if (businessEntity == null) {
            throw new DuplicateBusinessException("Business with the given ID does not exist.");
        }

        businessMapper.updateEntity(businessRequest, businessEntity);
        businessRepository.save(businessEntity);

        return businessMapper.toDto(businessEntity);
    }

    public void deleteBusiness(String businessId) {

        if(!businessRepository.existsById(businessId)){
            throw new BusinessNotFoundException("Business with the given ID does not exist.");
        }

        var businessEntity = businessRepository.findById(businessId).orElseThrow(BusinessNotFoundException::new);
        businessRepository.delete(businessEntity);
    }
}
