package com.example.FlightBooking.Components.ChainOfResponsibility;

import com.example.FlightBooking.DTOs.Request.Auth.SignInDTO;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.AuthJWT.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHandler extends BaseHandler {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void handle(SignInDTO request) throws Exception {
        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new Exception("User not found."));

        String jwtToken = jwtService.generateToken(user);
        System.out.println("JWT Token generated: " + jwtToken);
        forward(request);
    }
}
