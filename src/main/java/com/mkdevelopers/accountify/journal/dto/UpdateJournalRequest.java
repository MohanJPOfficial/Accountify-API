package com.mkdevelopers.accountify.journal.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateJournalRequest {

    @Size(min = 1, max = 255, message = "Journal name must be between 1 and 255 characters when provided")
    private String journalName;
}
