package com.example.FlightBooking.Controller.Booking;

import com.example.FlightBooking.DTOs.Request.Booking.BookingRequestDTO;
import com.example.FlightBooking.DTOs.Request.Booking.SelectSeatDTO;
import com.example.FlightBooking.Models.Booking;
import com.example.FlightBooking.Services.Planes.PlaneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/booking")
@CrossOrigin
@Tag(name = "Booking Ticket", description = "APIs for choose position sit down at once flight")
public class BookingController {

    @Autowired
    private PlaneService planeService;

    @PostMapping("/calculate-total-price")
    public ResponseEntity<Double> calculateTotalPrice(@RequestBody SelectSeatDTO selectedSeatsDTO) {
        try {
            double totalPrice = planeService.calculateTotalPrice(selectedSeatsDTO);
            return ResponseEntity.ok(totalPrice);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/hold-seats")
    public ResponseEntity<Boolean> holdSeats(@RequestParam Long planeId, @RequestBody Set<String> seatNumbers, @RequestBody Long userId) {
        try {
            boolean result = planeService.holdSeats(planeId, seatNumbers, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    @PostMapping("/book-seats")
    public ResponseEntity<Boolean> bookSeats(@RequestParam Long planeId, @RequestBody Set<String> seatNumbers, @RequestBody Long userId) {
        try {
            boolean result = planeService.bookSeats(planeId, seatNumbers, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }
    @PostMapping("/create-booking")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {
        try {
            Booking booking = planeService.createBooking(bookingRequestDTO);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
