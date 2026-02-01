package com.mkdevelopers.accountify.journal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateJournalRequest {

    @NotBlank(message = "Journal id is required")
    private String journalId;

    @NotBlank(message = "Journal name is required")
    private String journalName;

    @NotBlank(message = "Business id is required")
    private String businessId;

    @NotNull(message = "Timestamp is required")
    private long timestamp;
}
