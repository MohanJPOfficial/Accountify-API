package com.mkdevelopers.accountify.bill.dto;

import lombok.Data;

@Data
public class UpdateBillRequest {

    private String billNumber;
    private String billName;
    private String date;
    private String gstNo;
    private String location;
    private String stateCode;
}
