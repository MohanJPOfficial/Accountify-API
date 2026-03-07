package com.mkdevelopers.accountify.entry.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateLedgerEntryRequest {

    private String date;
    private String particular;
    @Pattern(regexp = "^(IN|OUT)$", message = "Particular type must be either IN or OUT")
    private String particularType;
    private Long transactionValue;
}
