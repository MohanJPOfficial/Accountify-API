package com.mkdevelopers.accountify.journal.mapper;

import com.mkdevelopers.accountify.journal.dto.CreateJournalRequest;
import com.mkdevelopers.accountify.journal.dto.JournalDto;
import com.mkdevelopers.accountify.journal.dto.UpdateJournalRequest;
import com.mkdevelopers.accountify.journal.entity.JournalEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JournalMapper {

    @Mapping(target = "business", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "entries", ignore = true)
    JournalEntity toEntity(CreateJournalRequest request);

    @Mapping(source = "business.businessId", target = "businessId")
    JournalDto toDto(JournalEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateJournalRequest request, @MappingTarget JournalEntity entity);
}
