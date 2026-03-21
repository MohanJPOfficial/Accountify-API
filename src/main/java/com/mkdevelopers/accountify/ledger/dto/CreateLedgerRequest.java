package com.mkdevelopers.accountify.ledger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.mkdevelopers.accountify.ledger.enums.LedgerType;

@Data
public class CreateLedgerRequest {

    @NotBlank(message = "Ledger id is required")
    private String ledgerId;

    @NotBlank(message = "Business id is required")
    private String businessId;

    @NotNull(message = "Ledger type is required")
    private LedgerType ledgerType;

    @NotBlank(message = "Business name is required")
    private String businessName;

    private String gstNo;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Timestamp is required")
    private Long timestamp;
}
