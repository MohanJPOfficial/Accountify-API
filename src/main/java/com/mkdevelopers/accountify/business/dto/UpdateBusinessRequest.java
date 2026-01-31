package com.mkdevelopers.accountify.business.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBusinessRequest {

    @Size(min = 1, max = 255, message = "Business name must be between 1 and 255 characters")
    private String businessName;

    @Size(max = 255, message = "GST number must not exceed 255 characters")
    private String gstNo;

    @Size(min = 1, max = 255, message = "Location must be between 1 and 255 characters when provided")
    private String location;
}
