package com.example.FlightBooking.Controller.Booking;

import com.example.FlightBooking.DTOs.Request.Booking.BookingRequestDTO;
import com.example.FlightBooking.DTOs.Request.Booking.SelectSeatDTO;
import com.example.FlightBooking.Models.Booking;
import com.example.FlightBooking.Services.BookingService.BookingService;
import com.example.FlightBooking.Services.FlightService.FlightService;
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
    private BookingService bookingService;
    //Cai nay la cai de tinh so tien khi chon ve ne
    @PostMapping("/calculate-total-price-after-booking")
    public ResponseEntity<Double> calculateTotalPrice(@RequestBody BookingRequestDTO bookingRequestDTO, @RequestParam String token) {
        try {
            double totalPrice = bookingService.calculateTotalPriceAfter(bookingRequestDTO.getFlightId(), (Set<String>) bookingRequestDTO.getSelectedSeats(), token);
            return ResponseEntity.ok(totalPrice);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    //Cai nay la cai de tinh so tien khi chon ve ne
    @PostMapping("/fill-info-passenger-to-create-booking")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO, @RequestParam String token) {
        try {
            Booking booking = bookingService.createBooking(bookingRequestDTO, token);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
