package com.example.FlightBooking.Controller.SignIn;

import com.example.FlightBooking.DTOs.Request.SignInDTO;
import com.example.FlightBooking.DTOs.Response.LoginResponse;
import com.example.FlightBooking.Models.Tokens;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.TokenRepository;
import com.example.FlightBooking.Services.AuthenticationService;
import com.example.FlightBooking.Services.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignInController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;
    private final TokenRepository tokenRepository;
    public SignInController(JwtService jwtService, AuthenticationService authenticationService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.tokenRepository = tokenRepository;
    }
    @PostMapping("/auth/signin")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody SignInDTO loginUserDto) {
        Users authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        Tokens tokens = new Tokens();
        tokens.setUserId(authenticatedUser);
        tokens.setToken(jwtToken);
        tokens.setExpireTime(jwtService.getExpirationTime());
        Tokens savedToken = tokenRepository.save(tokens);

        //
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
