package com.trillo.app.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trillo.app.dtos.BusinessDetailsResponse;
import com.trillo.app.dtos.HotspotPageResponse;
import com.trillo.app.services.HotspotService;

@RestController
@RequestMapping(path = {"/api/hotspots", "/hotspots"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class HotspotController {

    private final HotspotService hotspotService;

    public HotspotController(HotspotService hotspotService) {
        this.hotspotService = hotspotService;
    }

    @GetMapping
    public HotspotPageResponse list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return hotspotService.getHotspots(category, search, page, size);
    }

    @GetMapping("/{businessId}")
    public BusinessDetailsResponse getDetails(@PathVariable String businessId) {
        return hotspotService.getBusinessDetails(businessId);
    }
}
