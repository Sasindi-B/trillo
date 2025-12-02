package com.trillo.app.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trillo.app.entities.Booking;
import com.trillo.app.entities.BookingStatus;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByTravellerId(String travellerId);

    List<Booking> findByBusinessId(String businessId);

    boolean existsByBusinessIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String businessId,
            Collection<BookingStatus> statuses,
            LocalDate endDate,
            LocalDate startDate);
}
