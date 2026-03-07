package com.mkdevelopers.accountify.entry.mapper;

import com.mkdevelopers.accountify.entry.dto.EntryDto;
import com.mkdevelopers.accountify.entry.dto.JournalEntryRequest;
import com.mkdevelopers.accountify.entry.dto.LedgerEntryRequest;
import com.mkdevelopers.accountify.entry.dto.UpdateJournalEntryRequest;
import com.mkdevelopers.accountify.entry.dto.UpdateLedgerEntryRequest;
import com.mkdevelopers.accountify.entry.entity.EntryEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntryMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "journal", ignore = true)
    @Mapping(target = "ledger", ignore = true)
    EntryEntity toJournalEntryEntity(JournalEntryRequest request);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "journal", ignore = true)
    @Mapping(target = "ledger", ignore = true)
    EntryEntity toLedgerEntryEntity(LedgerEntryRequest request);

    @Mapping(source = "journal.journalId", target = "journalId")
    @Mapping(source = "ledger.ledgerId", target = "ledgerId")
    EntryDto toDto(EntryEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateJournalEntryEntity(UpdateJournalEntryRequest request, @MappingTarget EntryEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateLedgerEntryEntity(UpdateLedgerEntryRequest request, @MappingTarget EntryEntity entity);
}
