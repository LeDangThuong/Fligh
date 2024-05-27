package com.example.FlightBooking.Components.Observer;

import com.example.FlightBooking.Services.UserService.ObserverService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/observer")
@CrossOrigin
@Tag(name = "Observer Design Pattern", description = "...")
public class ObserverController {
    @Autowired
    private ObserverService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email) {
        userService.registerUser(email);
        return ResponseEntity.ok("Registration email sent.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        userService.requestPasswordReset(email);
        return ResponseEntity.ok("OTP sent to email.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam Long otp) {
        boolean isValid = userService.verifyOTP(email, otp);
        return isValid ? ResponseEntity.ok("OTP is valid.") : ResponseEntity.badRequest().body("Invalid OTP.");
    }

    @PostMapping("/book-ticket")
    public ResponseEntity<String> bookTicket(@RequestParam String email, @RequestParam String ticketDetails) {
        userService.bookTicket(email, ticketDetails);
        return ResponseEntity.ok("Ticket booking email sent.");
    }
}
