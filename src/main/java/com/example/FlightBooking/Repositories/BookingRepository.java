package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Booking;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;
@Repository
@Hidden
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByFlightId(Long flightId);
    List<Booking> findAllByUserId(Long userId);
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.createdAt BETWEEN :startDate AND :endDate")
    Long countBookingsByDateRange(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}
