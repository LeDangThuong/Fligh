package com.example.FlightBooking.Controller.Flights;

import com.example.FlightBooking.DTOs.Request.Flight.FlightDTO;
import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Services.FlightService.ExcelService;
import com.example.FlightBooking.Services.FlightService.FlightService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@RestController
@CrossOrigin
@Tag(name = "Flight CRUD", description = "APIs for create, read, update, delete flights")
@RequestMapping("/flight")
public class CRUDFlightController {
    @Autowired
    private ExcelService excelService;
    @Autowired
    private FlightService flightService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        excelService.saveFlightsFromExcel(file);
        return "File uploaded and data saved successfully!";
    }

    @PostMapping("/create-new-flight")
    public ResponseEntity<Flights> createFlight(@RequestBody FlightDTO flightDTO) {
        Flights flight = flightService.createFlight(flightDTO);
        return ResponseEntity.ok(flight);
    }
    @GetMapping("/search-one-way")
    public ResponseEntity<List<Flights>> searchFlightOneWay(
            @RequestParam Long departureAirportId,
            @RequestParam Long arrivalAirportId,
            @RequestParam Timestamp departureDate) {
        List<Flights> flights = flightService.searchFlightOneWay(departureAirportId, arrivalAirportId, departureDate);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/search-round-trip")
    public ResponseEntity<List<Flights>> searchFlightRoundTrip(
            @RequestParam Long departureAirportId,
            @RequestParam Long arrivalAirportId,
            @RequestParam Timestamp departureStartDate,
            @RequestParam Timestamp departureEndDate) {
        List<Flights> flights = flightService.searchFlightRoundTrip(departureAirportId, arrivalAirportId, departureStartDate, departureEndDate);
        return ResponseEntity.ok(flights);
    }
}
