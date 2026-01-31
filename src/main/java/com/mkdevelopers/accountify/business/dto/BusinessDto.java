package com.mkdevelopers.accountify.business.dto;

import com.mkdevelopers.accountify.business.entity.BusinessEntity;

import java.io.Serializable;

/**
 * DTO for {@link BusinessEntity}
 */
public record BusinessDto(
        String businessId,
        String businessName,
        String gstNo,
        String location,
        Long timestamp
) implements Serializable {

}