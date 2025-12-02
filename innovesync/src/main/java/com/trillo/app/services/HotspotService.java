package com.trillo.app.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trillo.app.dtos.BusinessDetailsResponse;
import com.trillo.app.dtos.HotspotPageResponse;
import com.trillo.app.dtos.HotspotResponse;
import com.trillo.app.entities.BusinessOwner;
import com.trillo.app.entities.BusinessProfile;
import com.trillo.app.repositories.AppBusinessOwnerRepository;
import com.trillo.app.repositories.BusinessProfileRepository;

@Service
public class HotspotService {

    private final AppBusinessOwnerRepository ownerRepository;
    private final BusinessProfileRepository profileRepository;

    public HotspotService(AppBusinessOwnerRepository ownerRepository, BusinessProfileRepository profileRepository) {
        this.ownerRepository = ownerRepository;
        this.profileRepository = profileRepository;
    }

    public HotspotPageResponse getHotspots(String category, String search, int page, int size) {
        List<BusinessOwner> owners = ownerRepository.findAll();

        List<BusinessOwner> filtered = owners.stream()
                .filter(o -> filterCategory(o, category))
                .filter(o -> filterSearch(o, search))
                .sorted(Comparator.comparing(BusinessOwner::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        int start = Math.min(page * size, filtered.size());
        int end = Math.min(start + size, filtered.size());
        List<BusinessOwner> slice = filtered.subList(start, end);
        List<HotspotResponse> items = slice.stream()
                .map(this::toHotspotResponse)
                .toList();

        int totalPages = (int) Math.ceil((double) filtered.size() / size);
        return new HotspotPageResponse(items, page, size, filtered.size(), totalPages);
    }

    public BusinessDetailsResponse getBusinessDetails(String businessId) {
        BusinessOwner owner = ownerRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));
        Optional<BusinessProfile> profileOpt = profileRepository.findByOwnerId(owner.getId());
        List<String> images = profileOpt.map(BusinessProfile::getImages)
                .orElseGet(List::of)
                .stream()
                .limit(6)
                .map(img -> img.getUrl())
                .collect(Collectors.toList());

        return new BusinessDetailsResponse(
                owner.getId(),
                owner.getName(),
                owner.getCategory(),
                owner.getLocation(),
                profileOpt.map(BusinessProfile::getDescription).orElse(""),
                profileOpt.map(BusinessProfile::getGoogleMapsUrl).orElse(""),
                images,
                owner.getEmail(),
                owner.getName(),
                List.of());
    }

    private HotspotResponse toHotspotResponse(BusinessOwner owner) {
        Optional<BusinessProfile> profileOpt = profileRepository.findByOwnerId(owner.getId());
        String preview = profileOpt
                .flatMap(p -> p.getImages().stream().findFirst())
                .map(img -> img.getUrl())
                .orElse("");
        return new HotspotResponse(
                owner.getId(),
                owner.getName(),
                owner.getCategory(),
                owner.getLocation(),
                preview,
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
        return contains(owner.getName(), needle)
                || contains(owner.getCategory(), needle)
                || contains(owner.getLocation(), needle);
    }

    private boolean contains(String value, String needle) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(needle);
    }
}
