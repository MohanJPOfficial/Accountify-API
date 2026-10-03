package com.mkdevelopers.accountify.entry.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateJournalEntryRequest {

    private String date;
    private String particular;
    @Pattern(regexp = "^(IN|OUT)$", message = "Particular type must be either IN or OUT")
    private String particularType;
    @Positive(message = "Transaction value must be greater than zero")
    private Long transactionValue;
}
