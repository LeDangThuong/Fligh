package com.example.FlightBooking.Controller.Airline;

import com.example.FlightBooking.DTOs.Request.AirlineAndAirport.AirlineRequest;
import com.example.FlightBooking.Models.Airlines;
import com.example.FlightBooking.Models.Planes;
import com.example.FlightBooking.Repositories.AirlinesRepository;
import com.example.FlightBooking.Services.AirlineService.AirlinesService;

import com.example.FlightBooking.Services.Planes.PlaneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@Tag(name ="CRUD FOR AIRLINE", description = "apis for changing AIRLINE info")
@RequestMapping("/airlines")
public class AirlinesController {
    @Autowired
    private AirlinesService airlinesService;
    @Autowired
    private AirlinesRepository airlinesRepository;

    @GetMapping
    public List<Airlines> getAllAirlines() {
        return airlinesService.getAllAirlines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airlines> getAirlinesById(@PathVariable Long id) {
        Optional<Airlines> airlines = airlinesService.getAirlinesById(id);
        return airlines.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Airlines addAirlines(@RequestBody Airlines airlines) {
        return airlinesService.addAirlines(airlines);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airlines> updateAirlines(@PathVariable Long id, @RequestBody Airlines airlinesDetails) {
        try {
            Airlines updatedAirlines = airlinesService.updateAirlines(id, airlinesDetails);
            return ResponseEntity.ok(updatedAirlines);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAirlines(@PathVariable Long id) {
        boolean isDeleted = airlinesService.deleteAirlines(id);
        if (isDeleted) {
            return ResponseEntity.ok("Delete successful");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Autowired
    private PlaneService planeService;

    @PostMapping(value = "/upload-new-airline", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createNewAirline(@RequestParam("airlineName") String airlineName,
                                              @RequestPart("file") MultipartFile file) {
        try {
            Airlines airline = airlinesService.createNewAirline(airlineName, file);
            return ResponseEntity.ok(airline);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading logo: " + e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(403).body("Error uploading logo: " + e.getMessage());
        }
    }
}
