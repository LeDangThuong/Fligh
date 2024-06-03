package com.example.FlightBooking.Controller.Flights;

import com.example.FlightBooking.DTOs.Request.Flight.FlightDTO;
import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Repositories.FlightRepository;
import com.example.FlightBooking.Services.FlightService.FlightService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin
@Tag(name = "Flight CRUD", description = "APIs for create, read, update, delete flights")
@RequestMapping("/flight")
public class CRUDFlightController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightRepository flightRepository;

    @PostMapping(value = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFlightData(@RequestPart("file") MultipartFile file, @RequestBody Long planeId) {
        try {
            flightService.uploadFlightData(file, planeId);
            return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-new-flight")
    public ResponseEntity<Flights> createFlight(@RequestBody FlightDTO flightDTO) throws JsonProcessingException {
        Flights flight = flightService.createFlight(flightDTO);
        return ResponseEntity.ok(flight);
    }
    @GetMapping("/search-flight-by-type")
    public ResponseEntity<List<Flights>> searchFlightOneWay(
            @RequestParam ("ROUND_TRIP or ONE_WAY")String flightType,
            @RequestParam Long departureAirportId,
            @RequestParam Long arrivalAirportId,
            @RequestParam Timestamp departureDate,
            @RequestParam (required = false) Timestamp returnDate) {
        List<Flights> flights = flightService.searchFlights(flightType ,departureAirportId, arrivalAirportId, departureDate, returnDate);
        return ResponseEntity.ok(flights);
    }
    @GetMapping( value = "/{flightId}/calculate-total-price", name = "Cái API này la lấy du lieu tong so tien truoc khi thanh toan")
    public double calculateTotalPrice(@PathVariable Long flightId,
                                      @RequestParam int numberOfTickets,
                                      @RequestParam String ticketType,
                                      @RequestParam boolean isRoundTrip) {
        return flightService.calculateTotalPrice(flightId, numberOfTickets, ticketType, isRoundTrip);
    }
    @GetMapping ("/get-flight-by-id")
    public Flights getFlightById(@RequestParam Long id)
    {
        return flightRepository.findById(id).orElseThrow(() -> new RuntimeException("Flight not found with this id: " + id));
    }
    // Cai nay la xem thu cai ghe do da duoc dat chua, hay la on hold theo user ID nao
    @GetMapping("/{flightId}/get-seat-status")
    public ResponseEntity<?> getSeatStatuses(@PathVariable Long planeId) {
        try {
            Map<String, Map<String, String>> seatStatuses = flightService.getSeatStatuses(planeId);
            return ResponseEntity.ok(seatStatuses);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting seat statuses: " + e.getMessage());
        }
    }

    //Khi nguoi dung booking ve thi hold ve cho nguoi dung
    @PostMapping("/{flightId}/hold")
    public ResponseEntity<?> holdSeats(@PathVariable Long flightId, @RequestBody Set<String> seatNumbers) {
        try {
            boolean success = flightService.holdSeats(flightId, seatNumbers);
            if (success) {
                return ResponseEntity.ok("Seats held successfully.");
            } else {
                return ResponseEntity.status(400).body("One or more seats are not available.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error holding seats: " + e.getMessage());
        }
    }
    // Dat ve may bay
    @PostMapping("/{flightId}/book")
    public ResponseEntity<?> bookSeats(@PathVariable Long flightId, @RequestBody Set<String> seatNumbers) {
        try {
            boolean success = flightService.bookSeats(flightId, seatNumbers);
            if (success) {
                return ResponseEntity.ok("Seats booked successfully.");
            } else {
                return ResponseEntity.status(400).body("One or more seats are not available for booking.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error booking seats: " + e.getMessage());
        }
    }
}
