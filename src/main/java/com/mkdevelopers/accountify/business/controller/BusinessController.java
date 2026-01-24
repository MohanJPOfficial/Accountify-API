package com.mkdevelopers.accountify.business.controller;

import com.mkdevelopers.accountify.business.dto.BusinessDto;
import com.mkdevelopers.accountify.business.dto.CreateBusinessRequest;
import com.mkdevelopers.accountify.business.dto.UpdateBusinessRequest;
import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.exception.DuplicateBusinessException;
import com.mkdevelopers.accountify.business.service.BusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

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

    @GetMapping
    public List<BusinessDto> getAllBusinesses() {
        return businessService.getAllBusinesses();
    }

    @PutMapping("/{id}")
    public BusinessDto updateBusiness(
            @PathVariable String id,
            @RequestBody UpdateBusinessRequest request
    ) {
        return businessService.updateBusiness(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteBusiness(
            @PathVariable String id
    ) {
        businessService.deleteBusiness(id);
    }

    @ExceptionHandler(BusinessNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBusinessNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(DuplicateBusinessException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateBusiness(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }
}
