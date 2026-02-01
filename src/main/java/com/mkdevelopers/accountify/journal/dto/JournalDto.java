package com.mkdevelopers.accountify.journal.dto;

import java.io.Serializable;

public record JournalDto(
        String journalId,
        String journalName,
        String businessId,
        Long timestamp
) implements Serializable {
}
