package com.mkdevelopers.accountify.business.dto;

import lombok.Data;

@Data
public class UpdateBusinessRequest {

    private String businessName;
    private String gstNo;
    private String location;
}
