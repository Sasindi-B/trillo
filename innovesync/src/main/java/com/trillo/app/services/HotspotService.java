package com.trillo.app.services;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trillo.app.dtos.BusinessDetailsResponse;
import com.trillo.app.dtos.HotspotPageResponse;
import com.trillo.app.dtos.HotspotResponse;
import com.trillo.innovesync.businessowner.BusinessOwner;
import com.trillo.innovesync.businessowner.BusinessOwnerProfile;
import com.trillo.innovesync.businessowner.BusinessOwnerProfileRepository;
import com.trillo.innovesync.businessowner.BusinessOwnerRepository;

@Service
public class HotspotService {

    private final BusinessOwnerRepository ownerRepository;
    private final BusinessOwnerProfileRepository profileRepository;

    public HotspotService(BusinessOwnerRepository ownerRepository, BusinessOwnerProfileRepository profileRepository) {
        this.ownerRepository = ownerRepository;
        this.profileRepository = profileRepository;
    }

    public HotspotPageResponse getHotspots(String category, String search, int page, int size) {
        // Fetch all business owners from MongoDB (always fresh data)
        List<BusinessOwner> owners = ownerRepository.findAll();

        List<BusinessOwner> filtered = owners.stream()
                .filter(o -> filterCategory(o, category))
                .filter(o -> filterSearch(o, search))
                .sorted(Comparator.comparing(BusinessOwner::getBusinessName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        int start = Math.min(page * size, filtered.size());
        int end = Math.min(start + size, filtered.size());
        List<BusinessOwner> slice = filtered.subList(start, end);
        
        // Map to response - this fetches fresh profile data for each owner
        List<HotspotResponse> items = slice.stream()
                .map(this::toHotspotResponse)
                .toList();

        int totalPages = (int) Math.ceil((double) filtered.size() / size);
        return new HotspotPageResponse(items, page, size, filtered.size(), totalPages);
    }

    public BusinessDetailsResponse getBusinessDetails(String businessId) {
        BusinessOwner owner = ownerRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));
        Optional<BusinessOwnerProfile> profileOpt = profileRepository.findByOwnerId(owner.getId());
        List<String> images = profileOpt.map(BusinessOwnerProfile::getImageUrls)
                .orElseGet(List::of)
                .stream()
                .limit(6)
                .collect(Collectors.toList());

        return new BusinessDetailsResponse(
                owner.getId(),
                owner.getBusinessName(),
                owner.getCategory(),
                owner.getLocation(),
                profileOpt.map(BusinessOwnerProfile::getDescription).orElse(""),
                profileOpt.map(BusinessOwnerProfile::getGoogleMapsUrl).orElse(""),
                images,
                owner.getEmail(),
                owner.getBusinessName(),
                List.of());
    }

    private HotspotResponse toHotspotResponse(BusinessOwner owner) {
        // Always fetch fresh profile data from MongoDB
        Optional<BusinessOwnerProfile> profileOpt = profileRepository.findByOwnerId(owner.getId());
        
        // Get preview image from profile (first image if available)
        String preview = profileOpt
                .map(BusinessOwnerProfile::getImageUrls)
                .orElseGet(List::of)
                .stream()
                .findFirst()
                .orElse("");
        
        // Return response with updated location from BusinessOwner entity
        // (location is updated when business owner updates their profile)
        return new HotspotResponse(
                owner.getId(),
                owner.getBusinessName(),
                owner.getCategory(),
                owner.getLocation(), // This reflects updates made via profile endpoint
                preview, // This comes from BusinessOwnerProfile
                owner.getId(),
                owner.getCreatedAt());
    }

    private boolean filterCategory(BusinessOwner owner, String category) {
        if (!StringUtils.hasText(category)) {
            return true;
        }
        return category.equalsIgnoreCase(owner.getCategory());
    }

    private boolean filterSearch(BusinessOwner owner, String search) {
        if (!StringUtils.hasText(search)) {
            return true;
        }
        String needle = search.toLowerCase(Locale.ROOT);
        boolean matchesBasic = contains(owner.getBusinessName(), needle)
                || contains(owner.getCategory(), needle)
                || contains(owner.getLocation(), needle);
        
        // Also search in profile description if available
        if (!matchesBasic) {
            Optional<BusinessOwnerProfile> profileOpt = profileRepository.findByOwnerId(owner.getId());
            if (profileOpt.isPresent()) {
                BusinessOwnerProfile profile = profileOpt.get();
                matchesBasic = contains(profile.getDescription(), needle);
            }
        }
        
        return matchesBasic;
    }

    private boolean contains(String value, String needle) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(needle);
    }
}
