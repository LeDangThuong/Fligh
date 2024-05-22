package com.example.FlightBooking.Controller.Seat;

import com.example.FlightBooking.Models.Booking;
import com.example.FlightBooking.Models.Seats;
import com.example.FlightBooking.Services.BookingService.BookingService;
import com.example.FlightBooking.Services.SeatService.SeatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/seats")
public class SeatController {
    @Autowired
    private SeatService seatService;
    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Seats> getAllSeats() {
        return seatService.getAllSeats();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seats> getSeatById(@PathVariable Long id) {
        Optional<Seats> seat = seatService.getSeatById(id);
        return seat.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Seats addSeat(@RequestBody Seats seat) {
        return seatService.addSeat(seat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seats> updateSeat(@PathVariable Long id, @RequestBody Seats seatDetails) {
        try {
            Seats updatedSeat = seatService.updateSeat(id, seatDetails);
            return ResponseEntity.ok(updatedSeat);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeat(@PathVariable Long id) {
        if(seatService.deleteSeat(id))
            return ResponseEntity.ok("Delete successful");
        else
            return ResponseEntity.notFound().build();
    }
    @GetMapping("/available")
    public ResponseEntity<List<Seats>> getAvailableSeats(@RequestParam Long flightId) {
        List<Seats> seats = seatService.getAvailableSeats(flightId);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/available/byClass")
    public ResponseEntity<List<Seats>> getAvailableSeatsByClass(@RequestParam Long flightId, @RequestParam String seatClass) {
        List<Seats> seats = seatService.getAvailableSeatsByClass(flightId, seatClass);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/available/byClassAndPosition")
    public ResponseEntity<List<Seats>> getAvailableSeatsByClassAndPosition(@RequestParam Long flightId, @RequestParam String seatClass, @RequestParam String seatPosition) {
        List<Seats> seats = seatService.getAvailableSeatsByClassAndPosition(flightId, seatClass, seatPosition);
        return ResponseEntity.ok(seats);
    }

    @PostMapping("/hold")
    public ResponseEntity<Seats> holdSeat(@RequestParam Long seatId) {
        Seats seat = seatService.holdSeat(seatId);
        return ResponseEntity.ok(seat);
    }

    @PostMapping("/book")
    public ResponseEntity<Seats> bookSeat(@RequestParam Long seatId, @RequestParam Long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId); // Add this method to BookingService
        Seats seat = seatService.bookSeat(seatId, booking);
        return ResponseEntity.ok(seat);
    }
}
