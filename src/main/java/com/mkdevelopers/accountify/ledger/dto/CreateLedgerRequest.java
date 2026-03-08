package com.mkdevelopers.accountify.ledger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateLedgerRequest {

    @NotBlank(message = "Ledger id is required")
    private String ledgerId;

    @NotBlank(message = "Business id is required")
    private String businessId;

    @NotBlank(message = "Ledger type is required")
    @Pattern(regexp = "^(PURCHASE|SALES)$", message = "Ledger type must be either Purchase or Sales")
    private String ledgerType;

    @NotBlank(message = "Business name is required")
    private String businessName;

    private String gstNo;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Timestamp is required")
    private Long timestamp;
}
