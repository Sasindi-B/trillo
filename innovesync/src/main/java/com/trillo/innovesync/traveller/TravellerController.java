package com.trillo.innovesync.traveller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/travellers")
@Validated
public class TravellerController {

    private final TravellerService travellerService;

    public TravellerController(TravellerService travellerService) {
        this.travellerService = travellerService;
    }

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TravellerSignupResponse> signupJson(@Valid @RequestBody TravellerSignupRequest request) {
        return ResponseEntity.status(201).body(travellerService.signup(request));
    }

    @PostMapping(path = "/signup", consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<TravellerSignupResponse> signupForm(@Valid @ModelAttribute TravellerSignupRequest request) {
        return ResponseEntity.status(201).body(travellerService.signup(request));
    }

    @GetMapping(path = "/{idOrEmail}/settings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TravellerSettingsResponse> getSettings(@PathVariable String idOrEmail) {
        return ResponseEntity.ok(travellerService.getSettings(idOrEmail));
    }

    @PutMapping(path = "/{idOrEmail}/settings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TravellerSettingsResponse> updateSettings(
            @PathVariable String idOrEmail,
            @Valid @RequestBody TravellerSettingsRequest request) {
        return ResponseEntity.ok(travellerService.updateSettings(idOrEmail, request));
    }
}
