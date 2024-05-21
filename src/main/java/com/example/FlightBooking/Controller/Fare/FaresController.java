package com.example.FlightBooking.Controller.Fare;

import com.example.FlightBooking.Models.Fares;
import com.example.FlightBooking.Services.FareService.FaresService;

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
@RequestMapping("/fares")
public class FaresController {
    @Autowired
    private FaresService faresService;

    @GetMapping
    public List<Fares> getAllFares() {
        return faresService.getAllFares();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fares> getFareById(@PathVariable Long id) {
        Optional<Fares> fare = faresService.getFareById(id);
        return fare.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Fares addFare(@RequestBody Fares fare) {
        return faresService.addFare(fare);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fares> updateFare(@PathVariable Long id, @RequestBody Fares fareDetails) {
        try {
            Fares updatedFare = faresService.updateFare(id, fareDetails);
            return ResponseEntity.ok(updatedFare);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFare(@PathVariable Long id) {
        boolean isDeleted = faresService.deleteFare(id);
        if (isDeleted) {
            return ResponseEntity.ok("Delete successful");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
