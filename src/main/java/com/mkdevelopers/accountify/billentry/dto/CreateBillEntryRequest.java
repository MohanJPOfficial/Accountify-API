package com.mkdevelopers.accountify.billentry.dto;

import com.mkdevelopers.accountify.billentry.constant.EntryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateBillEntryRequest {

    @NotBlank(message = "Bill entry id is required")
    private String billEntryId;

    @NotBlank(message = "Bill id is required")
    private String billId;

    @NotBlank(message = "Particular is required")
    private String particular;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Long amount;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    @NotNull(message = "Entry type is required")
    private EntryType entryType;

    private String returnDate;

    @NotNull(message = "Timestamp is required")
    private Long timestamp;
}
