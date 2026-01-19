package com.mkdevelopers.accountify.business.service;

import com.mkdevelopers.accountify.business.dto.BusinessDto;
import com.mkdevelopers.accountify.business.dto.CreateBusinessRequest;
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

    public void createBusiness(CreateBusinessRequest businessRequest) {

        // todo
    }

    public List<BusinessDto> getAllBusinesses() {

        var user = new UserEntity(
                "1",
                "Mohan",
                "",
                Collections.emptySet()
        );

        var businessEntities = businessRepository.getAllBusinessesByUser(user);
        return businessEntities.stream()
                .map(businessMapper::toDto)
                .toList();
    }
}
