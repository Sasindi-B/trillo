package com.trillo.app.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trillo.app.dtos.BookingRequest;
import com.trillo.app.dtos.BookingResponse;
import com.trillo.app.entities.Booking;
import com.trillo.app.entities.BookingStatus;
import com.trillo.app.entities.NotificationType;
import com.trillo.app.repositories.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;
    private final PaymentService paymentService;

    public BookingService(BookingRepository bookingRepository,
            NotificationService notificationService,
            PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.notificationService = notificationService;
        this.paymentService = paymentService;
    }

    public BookingResponse createBooking(BookingRequest request) {
        validateRequest(request);
        Booking booking = new Booking();
        booking.setBusinessId(request.getBusinessId());
        booking.setTravellerId(request.getTravellerId());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setNumberOfPeople(request.getNumberOfPeople());
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);

        // notify business owner
        notificationService.notifyUser(request.getBusinessId(),
                "New booking request",
                "You have a new booking request",
                NotificationType.BOOKING_REQUEST);

        return toResponse(booking);
    }

    public List<BookingResponse> getByTraveller(String travellerId) {
        return bookingRepository.findByTravellerId(travellerId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<BookingResponse> getByBusiness(String ownerId) {
        return bookingRepository.findByBusinessId(ownerId).stream()
                .map(this::toResponse)
                .toList();
    }

    public BookingResponse accept(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setStatus(BookingStatus.ACCEPTED);
        bookingRepository.save(booking);

        notificationService.notifyUser(booking.getTravellerId(),
                "Booking accepted",
                "Your booking has been accepted",
                NotificationType.BOOKING_ACCEPTED);
        paymentService.ensurePayment(booking);
        return toResponse(booking);
    }

    public BookingResponse decline(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setStatus(BookingStatus.DECLINED);
        bookingRepository.save(booking);

        notificationService.notifyUser(booking.getTravellerId(),
                "Booking declined",
                "Your booking has been declined",
                NotificationType.BOOKING_DECLINED);
        return toResponse(booking);
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getBusinessId(),
                booking.getTravellerId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getNumberOfPeople(),
                booking.getStatus(),
                booking.getCreatedAt());
    }

    private void validateRequest(BookingRequest request) {
        if (!StringUtils.hasText(request.getBusinessId()) || !StringUtils.hasText(request.getTravellerId())) {
            throw new IllegalArgumentException("BusinessId and travellerId are required");
        }
        if (request.getStartDate() == null || request.getEndDate() == null
                || request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Invalid date range");
        }
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if (request.getNumberOfPeople() <= 0) {
            throw new IllegalArgumentException("numberOfPeople must be positive");
        }
        if (request.getBusinessId().equals(request.getTravellerId())) {
            throw new IllegalArgumentException("Business owner cannot book themselves");
        }
        boolean overlap = bookingRepository.existsByBusinessIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                request.getBusinessId(),
                List.of(BookingStatus.PENDING, BookingStatus.ACCEPTED),
                request.getEndDate(),
                request.getStartDate());
        if (overlap) {
            throw new IllegalArgumentException("Overlapping booking");
        }
    }
}
