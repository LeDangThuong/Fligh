package com.example.FlightBooking.Controller.Auth.Veritification;

import com.example.FlightBooking.Services.VerificationService.VerificationService;

import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@CrossOrigin(value = "*")
public class VeriPasswordController {
    @Autowired
    VerificationService verificationService;
    @PostMapping("/auth/check-otp")
        public ResponseEntity<String> checkOTP(@RequestParam Long codeOTP, @RequestParam String email) {
            if (verificationService.checkOTP(codeOTP, email)) {
                return ResponseEntity.ok("OTP is valid");
            } else {
                return ResponseEntity.badRequest().body("Invalid OTP or expired");
            }
        }
}
