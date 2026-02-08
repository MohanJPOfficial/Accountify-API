package com.mkdevelopers.accountify.entry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateEntryRequest {

    @NotBlank(message = "Entry id is required")
    private String entryId;

    private String journalId;

    private String ledgerId;

    @NotBlank(message = "Date is required")
    private String date;

    @NotBlank(message = "Particular is required")
    private String particular;

    @NotBlank(message = "Particular type is required")
    @Pattern(regexp = "^(IN|OUT)$", message = "Particular type must be either IN or OUT")
    private String particularType;

    @NotNull(message = "Transaction value is required")
    private Long transactionValue;

    @NotNull(message = "Timestamp is required")
    private Long timestamp;
}
