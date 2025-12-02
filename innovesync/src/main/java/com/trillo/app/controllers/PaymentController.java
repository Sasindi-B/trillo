package com.trillo.app.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trillo.app.dtos.PaymentResponse;
import com.trillo.app.services.PaymentService;

@RestController
@RequestMapping(path = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{bookingId}")
    public PaymentResponse getPayment(@PathVariable String bookingId) {
        return paymentService.getByBookingId(bookingId);
    }

    @PostMapping("/{bookingId}/complete")
    public PaymentResponse complete(@PathVariable String bookingId) {
        return paymentService.complete(bookingId);
    }
}
