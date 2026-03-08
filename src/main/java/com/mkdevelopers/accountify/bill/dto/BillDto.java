package com.mkdevelopers.accountify.bill.dto;

import java.io.Serializable;

public record BillDto(
        String billId,
        String businessId,
        String ledgerId,
        String billNumber,
        String billName,
        String date,
        String gstNo,
        String location,
        Double taxRate,
        String taxType,
        String stateCode,
        String timestamp) implements Serializable {
}
