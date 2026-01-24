package com.mkdevelopers.accountify.business.mapper;

import com.mkdevelopers.accountify.business.dto.BusinessDto;
import com.mkdevelopers.accountify.business.dto.CreateBusinessRequest;
import com.mkdevelopers.accountify.business.dto.UpdateBusinessRequest;
import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BusinessMapper {
    BusinessEntity toEntity(CreateBusinessRequest businessRequest);

    BusinessDto toDto(BusinessEntity businessEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateBusinessRequest request, @MappingTarget BusinessEntity businessEntity);
}