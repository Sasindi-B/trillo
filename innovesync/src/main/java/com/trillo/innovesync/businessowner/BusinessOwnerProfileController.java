package com.trillo.innovesync.businessowner;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/business-owners/{idOrEmail}/profile")
@Validated
public class BusinessOwnerProfileController {

    private final BusinessOwnerProfileService profileService;

    public BusinessOwnerProfileController(BusinessOwnerProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessOwnerProfileResponse> getProfile(@PathVariable String idOrEmail) {
        return ResponseEntity.ok(profileService.getProfile(idOrEmail));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessOwnerProfileResponse> upsertProfile(
            @PathVariable String idOrEmail,
            @Valid @RequestBody BusinessOwnerProfileRequest request) {
        return ResponseEntity.ok(profileService.upsertProfile(idOrEmail, request));
    }

    @PostMapping(path = "/images", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessOwnerProfileResponse> addImages(
            @PathVariable String idOrEmail,
            @Valid @RequestBody BusinessOwnerImagesRequest request) {
        return ResponseEntity.ok(profileService.addImages(idOrEmail, request));
    }

    @PostMapping(path = "/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessOwnerProfileResponse> uploadImages(
            @PathVariable String idOrEmail,
            @RequestParam("files") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok(profileService.uploadImages(idOrEmail, files));
    }

    @DeleteMapping(path = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessOwnerProfileResponse> removeImage(
            @PathVariable String idOrEmail,
            @RequestParam("url") String imageUrl) {
        return ResponseEntity.ok(profileService.removeImage(idOrEmail, imageUrl));
    }

    @GetMapping(path = "/images/{fileId}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileId) throws IOException {
        GridFsResource resource = profileService.getImage(fileId);
        String contentType = resource.getContentType();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(resource);
    }
}
