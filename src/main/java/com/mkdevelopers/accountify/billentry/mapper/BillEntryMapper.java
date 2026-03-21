package com.mkdevelopers.accountify.billentry.mapper;

import com.mkdevelopers.accountify.billentry.dto.BillEntryDto;
import com.mkdevelopers.accountify.billentry.dto.CreateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.dto.UpdateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.entity.BillEntryEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BillEntryMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "bill", ignore = true)
    BillEntryEntity toEntity(CreateBillEntryRequest request);

    @Mapping(source = "bill.billId", target = "billId")
    BillEntryDto toDto(BillEntryEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "billEntryId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    void updateEntity(UpdateBillEntryRequest request, @MappingTarget BillEntryEntity entity);

}
