package com.mkdevelopers.accountify.ledger.dto;

import lombok.Data;

@Data
public class UpdateLedgerRequest {

    private String businessName;
    private String gstNo;
    private String location;
}
