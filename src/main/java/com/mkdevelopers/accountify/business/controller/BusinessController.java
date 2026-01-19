package com.mkdevelopers.accountify.business.controller;

import com.mkdevelopers.accountify.business.dto.BusinessDto;
import com.mkdevelopers.accountify.business.dto.CreateBusinessRequest;
import com.mkdevelopers.accountify.business.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/businesses")
class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<BusinessDto> createBusiness(
            @RequestBody CreateBusinessRequest createBusinessRequest
    ) {
        // todo
        return null;
    }
}
