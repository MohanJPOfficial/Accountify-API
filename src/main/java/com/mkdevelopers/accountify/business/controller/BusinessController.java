package com.mkdevelopers.accountify.business.controller;

import com.mkdevelopers.accountify.business.dto.BusinessDto;
import com.mkdevelopers.accountify.business.dto.CreateBusinessRequest;
import com.mkdevelopers.accountify.business.dto.UpdateBusinessRequest;
import com.mkdevelopers.accountify.business.service.BusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/businesses")
class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<BusinessDto> createBusiness(
            @Valid @RequestBody CreateBusinessRequest createBusinessRequest,
            UriComponentsBuilder uriBuilder
    ) {
        var businessDto = businessService.createBusiness(createBusinessRequest);
        var uri = uriBuilder.path("/businesses/{id}").buildAndExpand(businessDto.businessId()).toUri();
        return ResponseEntity.created(uri).body(businessDto);
    }

    @GetMapping("/{id}")
    public BusinessDto getBusinessById(@PathVariable String id) {
        return businessService.getBusinessById(id);
    }

    @GetMapping
    public Page<BusinessDto> getAllBusinesses(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return businessService.getAllBusinesses(pageable);
    }

    @PutMapping("/{id}")
    public BusinessDto updateBusiness(
            @PathVariable String id,
            @RequestBody UpdateBusinessRequest request
    ) {
        return businessService.updateBusiness(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable String id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
}
