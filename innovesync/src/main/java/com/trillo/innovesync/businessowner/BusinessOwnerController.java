package com.trillo.innovesync.businessowner;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/business-owners")
@Validated
public class BusinessOwnerController {

    private final BusinessOwnerService businessOwnerService;

    public BusinessOwnerController(BusinessOwnerService businessOwnerService) {
        this.businessOwnerService = businessOwnerService;
    }

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessOwnerSignupResponse> signupJson(
            @Valid @RequestBody BusinessOwnerSignupRequest request) {
        return ResponseEntity.status(201).body(businessOwnerService.signup(request));
    }

    @PostMapping(path = "/signup", consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<BusinessOwnerSignupResponse> signupForm(
            @Valid @ModelAttribute BusinessOwnerSignupRequest request) {
        return ResponseEntity.status(201).body(businessOwnerService.signup(request));
    }
}
