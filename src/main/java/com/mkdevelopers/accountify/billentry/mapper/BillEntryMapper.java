package com.mkdevelopers.accountify.billentry.mapper;

import com.mkdevelopers.accountify.billentry.constant.EntryType;
import com.mkdevelopers.accountify.billentry.dto.BillEntryDto;
import com.mkdevelopers.accountify.billentry.dto.CreateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.dto.UpdateBillEntryRequest;
import com.mkdevelopers.accountify.billentry.entity.BillEntryEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BillEntryMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "entryType", source = "entryType", qualifiedByName = "entryTypeToString")
    BillEntryEntity toEntity(CreateBillEntryRequest request);

    @Mapping(source = "bill.billId", target = "billId")
    @Mapping(target = "entryType", source = "entryType", qualifiedByName = "stringToEntryType")
    BillEntryDto toDto(BillEntryEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "billEntryId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "entryType", source = "entryType", qualifiedByName = "entryTypeToString")
    void updateEntity(UpdateBillEntryRequest request, @MappingTarget BillEntryEntity entity);

    @Named("entryTypeToString")
    default String entryTypeToString(EntryType entryType) {
        return entryType == null ? null : entryType.name();
    }

    @Named("stringToEntryType")
    default EntryType stringToEntryType(String entryType) {
        return entryType == null ? null : EntryType.valueOf(entryType);
    }
}
