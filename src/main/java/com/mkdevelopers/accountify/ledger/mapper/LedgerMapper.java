package com.mkdevelopers.accountify.ledger.mapper;

import com.mkdevelopers.accountify.ledger.dto.CreateLedgerRequest;
import com.mkdevelopers.accountify.ledger.dto.LedgerDto;
import com.mkdevelopers.accountify.ledger.dto.UpdateLedgerRequest;
import com.mkdevelopers.accountify.ledger.entity.LedgerEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LedgerMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "bills", ignore = true)
    @Mapping(target = "entries", ignore = true)
    LedgerEntity toEntity(CreateLedgerRequest request);

    @Mapping(source = "business.businessId", target = "businessId")
    LedgerDto toDto(LedgerEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "ledgerId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "ledgerType", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "bills", ignore = true)
    @Mapping(target = "entries", ignore = true)
    void updateEntity(UpdateLedgerRequest request, @MappingTarget LedgerEntity entity);
}
