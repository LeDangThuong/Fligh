package com.example.FlightBooking.Controller.Airline;

import com.example.FlightBooking.Models.Airlines;
import com.example.FlightBooking.Services.AirlineService.AirlinesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/airlines")
public class AirlinesController {
    @Autowired
    private AirlinesService airlinesService;

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
}
