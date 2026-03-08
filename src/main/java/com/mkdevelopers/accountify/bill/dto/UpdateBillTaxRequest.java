package com.mkdevelopers.accountify.bill.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateBillTaxRequest {

    @NotBlank(message = "Tax type is required")
    @Pattern(regexp = "^(GST_OR_SGST|IGST)$", message = "Tax type must be either GST_OR_SGST or IGST")
    private String taxType;

    @NotNull(message = "Tax rate is required")
    private Double taxRate;
}
