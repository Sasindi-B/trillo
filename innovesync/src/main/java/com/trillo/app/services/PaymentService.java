package com.trillo.app.services;

import org.springframework.stereotype.Service;

import com.trillo.app.dtos.PaymentResponse;
import com.trillo.app.entities.Booking;
import com.trillo.app.entities.Payment;
import com.trillo.app.entities.PaymentStatus;
import com.trillo.app.repositories.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse getByBookingId(String bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return toResponse(payment);
    }

    public PaymentResponse complete(String bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
        return toResponse(payment);
    }

    public void ensurePayment(Booking booking) {
        if (paymentRepository.findByBookingId(booking.getId()).isPresent()) {
            return;
        }
        Payment payment = new Payment();
        payment.setBookingId(booking.getId());
        payment.setTravellerId(booking.getTravellerId());
        payment.setAmount(0.0); // placeholder for Stripe/PayHere
        paymentRepository.save(payment);
    }

    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getBookingId(),
                p.getTravellerId(),
                p.getAmount(),
                p.getStatus(),
                p.getCreatedAt());
    }
}
