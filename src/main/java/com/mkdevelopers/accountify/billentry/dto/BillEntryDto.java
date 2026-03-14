package com.mkdevelopers.accountify.billentry.dto;

import com.mkdevelopers.accountify.billentry.constant.EntryType;

import java.io.Serializable;

public record BillEntryDto(
                String billEntryId,
                String billId,
                String particular,
                Long amount,
                Integer quantity,
                EntryType entryType,
                String returnDate,
                Long timestamp
) implements Serializable {
}
