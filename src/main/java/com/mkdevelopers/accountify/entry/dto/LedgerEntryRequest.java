package com.mkdevelopers.accountify.entry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LedgerEntryRequest {

    @NotBlank(message = "Entry id is required")
    private String entryId;

    @NotBlank(message = "Ledger id is required")
    private String ledgerId;

    @NotBlank(message = "Date is required")
    private String date;

    @NotBlank(message = "Particular is required")
    private String particular;

    @NotBlank(message = "Particular type is required")
    @Pattern(regexp = "^(IN|OUT)$", message = "Particular type must be either IN or OUT")
    private String particularType;

    @NotNull(message = "Transaction value is required")
    @Positive(message = "Transaction value must be greater than zero")
    private Long transactionValue;

    @NotNull(message = "Timestamp is required")
    private Long timestamp;
}
