package com.trillo.innovesync.businessowner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BusinessOwnerProfileService {

    private static final int MAX_IMAGES = 6;
    private static final Pattern IMAGE_ID_PATTERN = Pattern.compile(".*/images/([a-fA-F0-9]{24})$");

    private final BusinessOwnerRepository businessOwnerRepository;
    private final BusinessOwnerProfileRepository profileRepository;
    private final GridFsTemplate gridFsTemplate;

    public BusinessOwnerProfileService(BusinessOwnerRepository businessOwnerRepository,
            BusinessOwnerProfileRepository profileRepository,
            GridFsTemplate gridFsTemplate) {
        this.businessOwnerRepository = Objects.requireNonNull(businessOwnerRepository, "businessOwnerRepository");
        this.profileRepository = Objects.requireNonNull(profileRepository, "profileRepository");
        this.gridFsTemplate = Objects.requireNonNull(gridFsTemplate, "gridFsTemplate");
    }

    public BusinessOwnerProfileResponse getProfile(String idOrEmail) {
        BusinessOwner owner = findOwner(idOrEmail);
        BusinessOwnerProfile profile = profileRepository.findByOwnerId(owner.getId())
                .orElseGet(() -> new BusinessOwnerProfile(owner.getId()));
        return mapToResponse(owner, profile);
    }

    public BusinessOwnerProfileResponse upsertProfile(String idOrEmail, BusinessOwnerProfileRequest request) {
        Objects.requireNonNull(request, "request");
        BusinessOwner owner = findOwner(idOrEmail);
        String ownerId = owner.getId();
        BusinessOwnerProfile profile = profileRepository.findByOwnerId(ownerId)
                .orElseGet(() -> new BusinessOwnerProfile(ownerId));

        profile.setDescription(defaultString(request.getDescription()));
        profile.setGoogleMapsUrl(defaultString(request.getGoogleMapsUrl()));
        applyImages(profile, Optional.ofNullable(request.getImageUrls()).orElseGet(List::of));
        profile.touchUpdatedAt();

        // Update location if provided
        if (request.getLocation() != null) {
            String trimmedLocation = request.getLocation().trim();
            owner.setLocation(trimmedLocation);
            businessOwnerRepository.save(owner);
        }

        BusinessOwnerProfile saved = profileRepository.save(profile);
        return mapToResponse(owner, saved);
    }

    public BusinessOwnerProfileResponse addImages(String idOrEmail, BusinessOwnerImagesRequest request) {
        Objects.requireNonNull(request, "request");
        List<String> newImages = Optional.ofNullable(request.getImageUrls()).orElseGet(List::of);
        BusinessOwner owner = findOwner(idOrEmail);
        BusinessOwnerProfile profile = profileRepository.findByOwnerId(owner.getId())
                .orElseGet(() -> new BusinessOwnerProfile(owner.getId()));

        List<String> merged = new ArrayList<>(profile.getImageUrls());
        merged.addAll(newImages);
        enforceImageLimit(merged);
        profile.setImageUrls(merged);
        profile.touchUpdatedAt();

        BusinessOwnerProfile saved = profileRepository.save(profile);
        return mapToResponse(owner, saved);
    }

    public BusinessOwnerProfileResponse removeImage(String idOrEmail, String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            throw new IllegalArgumentException("Image URL is required");
        }
        BusinessOwner owner = findOwner(idOrEmail);
        BusinessOwnerProfile profile = profileRepository.findByOwnerId(owner.getId())
                .orElseGet(() -> new BusinessOwnerProfile(owner.getId()));

        List<String> remaining = new ArrayList<>(profile.getImageUrls());
        remaining.removeIf(url -> url.equals(imageUrl));
        deleteImageFileIfOwned(imageUrl);
        profile.setImageUrls(remaining);
        profile.touchUpdatedAt();

        BusinessOwnerProfile saved = profileRepository.save(profile);
        return mapToResponse(owner, saved);
    }

    public BusinessOwnerProfileResponse uploadImages(String idOrEmail, List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("No files provided");
        }
        BusinessOwner owner = findOwner(idOrEmail);
        BusinessOwnerProfile profile = profileRepository.findByOwnerId(owner.getId())
                .orElseGet(() -> new BusinessOwnerProfile(owner.getId()));

        List<String> current = new ArrayList<>(profile.getImageUrls());
        if (current.size() + files.size() > MAX_IMAGES) {
            throw new IllegalArgumentException("You can store up to " + MAX_IMAGES + " images");
        }

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            String storedId = storeFile(file);
            String url = "/api/business-owners/" + owner.getId() + "/profile/images/" + storedId;
            current.add(url);
        }

        profile.setImageUrls(current);
        profile.touchUpdatedAt();
        BusinessOwnerProfile saved = profileRepository.save(profile);
        return mapToResponse(owner, saved);
    }

    public GridFsResource getImage(String fileId) {
        GridFsResource resource = loadFile(fileId);
        if (resource == null || !resource.exists()) {
            throw new IllegalArgumentException("Image not found");
        }
        return resource;
    }

    private void applyImages(BusinessOwnerProfile profile, List<String> images) {
        enforceImageLimit(images);
        profile.setImageUrls(images);
    }

    private void enforceImageLimit(List<String> images) {
        if (images.size() > MAX_IMAGES) {
            throw new IllegalArgumentException("You can store up to " + MAX_IMAGES + " images");
        }
    }

    private BusinessOwner findOwner(String idOrEmail) {
        if (!StringUtils.hasText(idOrEmail)) {
            throw new IllegalArgumentException("Business owner id or email is required");
        }
        String trimmed = idOrEmail.trim();
        return (trimmed.contains("@")
                ? businessOwnerRepository.findByEmailIgnoreCase(normalizeEmail(trimmed))
                : businessOwnerRepository.findById(trimmed))
                .orElseThrow(() -> new IllegalArgumentException("Business owner not found: " + trimmed));
    }

    private String defaultString(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private BusinessOwnerProfileResponse mapToResponse(BusinessOwner owner, BusinessOwnerProfile profile) {
        return new BusinessOwnerProfileResponse(
                profile.getId(),
                owner.getId(),
                owner.getBusinessName(),
                owner.getEmail(),
                resolveLocation(owner),
                owner.getCategory(),
                profile.getDescription(),
                profile.getGoogleMapsUrl(),
                profile.getImageUrls());
    }

    private String resolveLocation(BusinessOwner owner) {
        // Return the exact location saved during signup or updated in profile from the database
        String ownerLocation = owner.getLocation();
        return ownerLocation == null ? "" : ownerLocation;
    }

    private String storeFile(MultipartFile file) throws IOException {
        ObjectId objectId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        return objectId.toHexString();
    }

    private GridFsResource loadFile(String fileId) {
        try {
            Query query = Query.query(Criteria.where("_id").is(new ObjectId(fileId)));
            return gridFsTemplate.getResource(gridFsTemplate.findOne(query));
        } catch (Exception ex) {
            return null;
        }
    }

    private void deleteImageFileIfOwned(String imageUrl) {
        Matcher matcher = IMAGE_ID_PATTERN.matcher(imageUrl);
        if (!matcher.matches()) {
            return;
        }
        String fileId = matcher.group(1);
        try {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(new ObjectId(fileId))));
        } catch (Exception ignored) {
            // swallow cleanup errors
        }
    }
}
