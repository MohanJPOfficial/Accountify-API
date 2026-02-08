package com.mkdevelopers.accountify.entry.dto;

import java.io.Serializable;

public record EntryDto(
        String entryId,
        String journalId,
        String ledgerId,
        String date,
        String particular,
        String particularType,
        Long transactionValue,
        Long timestamp
) implements Serializable {
}
