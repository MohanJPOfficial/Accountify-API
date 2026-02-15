package com.mkdevelopers.accountify.ledger.dto;

import java.io.Serializable;

public record LedgerDto(
        String ledgerId,
        String businessId,
        String ledgerType,
        String businessName,
        String gstNo,
        String location,
        Long timestamp
) implements Serializable {
}
