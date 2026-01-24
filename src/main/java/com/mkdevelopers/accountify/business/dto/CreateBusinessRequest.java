package com.mkdevelopers.accountify.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBusinessRequest {

    @NotBlank(message = "Business id is required")
    private String businessId;

    @NotBlank(message = "Business name is required")
    private String businessName;

    private String gstNo;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Timestamp is required")
    private long timestamp;
}
