package com.example.FlightBooking.Controller.SignUp;

import com.example.FlightBooking.DTOs.Request.SignUpDTO;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Services.AuthenticationService;
import com.example.FlightBooking.Services.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public SignUpController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<Users> register(@RequestBody SignUpDTO registerUserDto) {
        Users registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }
}
