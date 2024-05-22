package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Seats;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface SeatRepository extends JpaRepository<Seats, Long> {
//    List<Seats> findByFlightIdAndStatus(Long flightId, String status);
//    List<Seats> findByFlightIdAndSeatClassAndStatus(Long flightId, String seatClass, String status);
//    List<Seats> findByFlightIdAndSeatClassAndSeatPositionAndStatus(Long flightId, String seatClass, String position, String status);
}
