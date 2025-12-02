package com.trillo.app.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trillo.app.dtos.BookingRequest;
import com.trillo.app.dtos.BookingResponse;
import com.trillo.app.services.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookingResponse create(@Valid @RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @GetMapping("/traveller/{travellerId}")
    public List<BookingResponse> byTraveller(@PathVariable String travellerId) {
        return bookingService.getByTraveller(travellerId);
    }

    @GetMapping("/business/{ownerId}")
    public List<BookingResponse> byBusiness(@PathVariable String ownerId) {
        return bookingService.getByBusiness(ownerId);
    }

    @PutMapping("/{bookingId}/accept")
    public BookingResponse accept(@PathVariable String bookingId) {
        return bookingService.accept(bookingId);
    }

    @PutMapping("/{bookingId}/decline")
    public BookingResponse decline(@PathVariable String bookingId) {
        return bookingService.decline(bookingId);
    }
}
