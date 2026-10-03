package com.mkdevelopers.accountify.billentry.dto;

import com.mkdevelopers.accountify.billentry.constant.EntryType;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateBillEntryRequest {

    private String particular;
    @Positive(message = "Amount must be greater than zero")
    private Long amount;
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;
    private EntryType entryType;
    private String returnDate;
}
