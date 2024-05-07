package com.example.FlightBooking.Controller.Auth.SignUp;

import com.example.FlightBooking.DTOs.Request.Auth.SignUpDTO;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Services.AuthJWT.AuthenticationService;
import com.example.FlightBooking.Services.AuthJWT.JwtService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin (value = {"http://localhost:7050", "https://flightbookingbe-production.up.railway.app", "http://localhost:5173"})
@Tag(name = "Authentication", description = "APIs for authenticate for user")
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
