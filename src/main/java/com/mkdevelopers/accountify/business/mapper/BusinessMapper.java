package com.mkdevelopers.accountify.business.mapper;

import com.mkdevelopers.accountify.business.dto.BusinessDto;
import com.mkdevelopers.accountify.business.dto.CreateBusinessRequest;
import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BusinessMapper {
    BusinessEntity toEntity(BusinessDto businessDto);

    BusinessEntity toEntity(CreateBusinessRequest businessRequest);

    BusinessDto toDto(BusinessEntity businessEntity);
}