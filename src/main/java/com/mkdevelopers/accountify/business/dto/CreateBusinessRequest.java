package com.mkdevelopers.accountify.business.dto;

import lombok.Data;

@Data
public class CreateBusinessRequest {

    private String businessId;
    private String businessName;
    private String gstNo;
    private String location;
    private long timestamp;
}
