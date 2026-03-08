package com.mkdevelopers.accountify.bill.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBillRequest {

    @NotBlank(message = "Bill id is required")
    private String billId;

    private String businessId;

    private String ledgerId;

    @NotBlank(message = "Bill number is required")
    private String billNumber;

    @NotBlank(message = "Bill name is required")
    private String billName;

    @NotBlank(message = "Date is required")
    private String date;

    private String gstNo;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "State code is required")
    private String stateCode;

    @NotNull(message = "Timestamp is required")
    private Long timestamp;
}
