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
    //Cai nay la cai de tinh so tien khi chon ve ne
    @PostMapping("/calculate-total-price-before-booking")
    public ResponseEntity<Double> calculateTotalPrice(@RequestBody SelectSeatDTO selectedSeatsDTO) {
        try {
            double totalPrice = planeService.calculateTotalPrice(selectedSeatsDTO);
            return ResponseEntity.ok(totalPrice);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    //Cai nay la cai de tinh so tien khi chon ve ne
    @PostMapping("/hold-seats-when-booking")
    public ResponseEntity<Boolean> holdSeats(@RequestParam Long planeId, @RequestBody Set<String> seatNumbers) {
        try {
            boolean result = planeService.holdSeats(planeId, seatNumbers);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    @PostMapping("/select-seats-for-ticket")
    public ResponseEntity<Boolean> bookSeats(@RequestParam Long planeId, @RequestBody Set<String> seatNumbers) {
        try {
            boolean result = planeService.bookSeats(planeId, seatNumbers);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    @PostMapping("/fill-info-passenger-to-create-booking")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {
        try {
            Booking booking = planeService.createBooking(bookingRequestDTO);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
