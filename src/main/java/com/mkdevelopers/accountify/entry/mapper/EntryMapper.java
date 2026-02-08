package com.mkdevelopers.accountify.entry.mapper;

import com.mkdevelopers.accountify.entry.dto.CreateEntryRequest;
import com.mkdevelopers.accountify.entry.dto.EntryDto;
import com.mkdevelopers.accountify.entry.dto.UpdateEntryRequest;
import com.mkdevelopers.accountify.entry.entity.EntryEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntryMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "journal", ignore = true)
    @Mapping(target = "ledger", ignore = true)
    EntryEntity toEntity(CreateEntryRequest request);

    @Mapping(source = "journal.journalId", target = "journalId")
    @Mapping(source = "ledger.ledgerId", target = "ledgerId")
    EntryDto toDto(EntryEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateEntryRequest request, @MappingTarget EntryEntity entity);
}
