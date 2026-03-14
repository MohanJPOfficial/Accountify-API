package com.mkdevelopers.accountify.billentry.dto;

import com.mkdevelopers.accountify.billentry.constant.EntryType;
import lombok.Data;

@Data
public class UpdateBillEntryRequest {

    private String particular;
    private Long amount;
    private Integer quantity;
    private EntryType entryType;
    private String returnDate;
}
