package com.example.FlightBooking.Controller.Airline;

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
    public ResponseEntity<?> getAllPlane(@RequestParam Long airlineId)
    {
        try {
            List<Planes> plane = planeService.getAllPlanesByAirlineId(airlineId);
            return ResponseEntity.ok(plane);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting Plane detail: " + e.getMessage());
        }
    }
}
