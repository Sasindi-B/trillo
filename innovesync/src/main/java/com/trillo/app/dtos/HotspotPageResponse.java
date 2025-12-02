package com.trillo.app.dtos;

import java.time.Instant;
import java.util.List;

public class HotspotPageResponse {
    private List<HotspotResponse> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private Instant timestamp = Instant.now();

    public HotspotPageResponse(List<HotspotResponse> items, int page, int size, long totalElements, int totalPages) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<HotspotResponse> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
