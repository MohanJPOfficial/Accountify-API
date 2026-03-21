package com.mkdevelopers.accountify.ledger.dto;

import com.mkdevelopers.accountify.ledger.enums.LedgerType;
import java.io.Serializable;

public record LedgerDto(
                String ledgerId,
                String businessId,
                LedgerType ledgerType,
                String businessName,
                String gstNo,
                String location,
                Long timestamp) implements Serializable {
}
