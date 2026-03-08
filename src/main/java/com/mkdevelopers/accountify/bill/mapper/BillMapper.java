package com.mkdevelopers.accountify.bill.mapper;

import com.mkdevelopers.accountify.bill.dto.BillDto;
import com.mkdevelopers.accountify.bill.dto.CreateBillRequest;
import com.mkdevelopers.accountify.bill.dto.UpdateBillRequest;
import com.mkdevelopers.accountify.bill.dto.UpdateBillTaxRequest;
import com.mkdevelopers.accountify.bill.entity.BillEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BillMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "ledger", ignore = true)
    @Mapping(target = "taxRate", ignore = true)
    @Mapping(target = "taxType", ignore = true)
    @Mapping(target = "billEntries", ignore = true)
    BillEntity toEntity(CreateBillRequest request);

    @Mapping(source = "business.businessId", target = "businessId")
    @Mapping(source = "ledger.ledgerId", target = "ledgerId")
    BillDto toDto(BillEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "billId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "ledger", ignore = true)
    @Mapping(target = "taxRate", ignore = true)
    @Mapping(target = "taxType", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "billEntries", ignore = true)
    void updateEntity(UpdateBillRequest request, @MappingTarget BillEntity entity);

    @Mapping(target = "billId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "ledger", ignore = true)
    @Mapping(target = "billNumber", ignore = true)
    @Mapping(target = "billName", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "gstNo", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "stateCode", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "billEntries", ignore = true)
    void updateTaxEntity(UpdateBillTaxRequest request, @MappingTarget BillEntity entity);
}
