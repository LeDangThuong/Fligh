package com.example.FlightBooking.Controller.Airline;

import com.example.FlightBooking.DTOs.Response.Airline.PlaneResponse;
import com.example.FlightBooking.Models.Airlines;
import com.example.FlightBooking.Models.Planes;
import com.example.FlightBooking.Services.Planes.PlaneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/airlines")
@Tag(name ="CRUD FOR AIRLINE", description = "apis for changing AIRLINE info")
public class PlaneController {
    @Autowired
    private PlaneService planeService;

    @PostMapping("/create-new-plane")
    public ResponseEntity<?> createNewPlane(@RequestParam Long airlineId) {
        try {
            Planes plane = planeService.createPlaneWithSeats(airlineId);
            return ResponseEntity.ok(plane);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating plane: " + e.getMessage());
        }
    }

    @GetMapping("/get-plane-detail-by-planeId")
    public ResponseEntity<?> getDetailPlane(@RequestParam Long planeId)
    {
        try {
            Planes planes = planeService.getDetailPlane(planeId);
            return ResponseEntity.ok(planes);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting Plane detail: " + e.getMessage());
        }
    }
    @GetMapping("/get-all-plane-by-airline-id")
    public ResponseEntity<?> getAllPlane(@RequestParam Long airlineId) {
        try {
            List<Planes> planes = planeService.getAllPlanesByAirlineId(airlineId);
            List<PlaneResponse> planeResponses = planes.stream().map(this::convertToPlaneResponse).collect(Collectors.toList());
            return ResponseEntity.ok(planeResponses);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting Plane detail: " + e.getMessage());
        }
    }
    private PlaneResponse convertToPlaneResponse(Planes planes) {
        PlaneResponse response = new PlaneResponse();
        response.setId(planes.getId());
        response.setFlightNumber(planes.getFlightNumber());
        return response;
    }
}
