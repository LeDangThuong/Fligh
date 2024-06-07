package com.example.FlightBooking.Controller.Airline;

import com.example.FlightBooking.DTOs.Response.Airline.AirlineResponse;
import com.example.FlightBooking.Models.Airlines;
import com.example.FlightBooking.Repositories.AirlinesRepository;
import com.example.FlightBooking.Services.AirlineService.AirlinesService;

import com.example.FlightBooking.Services.Planes.PlaneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Transactional
    public List<AirlineResponse> getAllAirlines() {
        List<Airlines> airlinesList = airlinesService.getAllAirlines();
        return airlinesList.stream().map(this::convertToAirlineResponse).collect(Collectors.toList());
    }
    private AirlineResponse convertToAirlineResponse(Airlines airlines) {
        AirlineResponse response = new AirlineResponse();
        response.setId(airlines.getId());
        response.setAirlineName(airlines.getAirlineName());
        response.setLogoUrl(airlines.getLogoUrl());
        response.setPicture(airlines.getPromoForAirline());
        return response;
    }

    @GetMapping("/{id}")
    @Transactional
    public AirlineResponse getAirlinesById(@RequestParam Long id) {
        Optional<Airlines> airlines = airlinesService.getAirlinesById(id);
        return convertToAirlineResponse(airlines.get());
    }

    @PostMapping
    public Airlines addAirlines(@RequestBody Airlines airlines) {
        return airlinesService.addAirlines(airlines);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
    @Transactional
    public ResponseEntity<Airlines> updateAirlines(@RequestParam Long id, @RequestPart List<MultipartFile> files) {
        try {
            Airlines updatedAirlines = airlinesService.updateAirlines(id, files);
            return ResponseEntity.ok(updatedAirlines);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    @GetMapping("/get-airline-by-planeId")
    @Transactional
    public ResponseEntity<AirlineResponse> getAirlineByPlaneId(@RequestParam Long planeId)
    {
        return ResponseEntity.ok(convertToAirlineResponse(airlinesService.getAirlineByPlaneId(planeId)));
    }
}
