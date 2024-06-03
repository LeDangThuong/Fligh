package com.example.FlightBooking.Components.ChainOfResponsibility;

import com.example.FlightBooking.DTOs.Request.Auth.SignInDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final Handler chain;
    @Autowired
    public LoginService(ValidationHandler validationHandler, AuthenticationHandler authenticationHandler, JwtTokenHandler jwtTokenHandler) {
        this.chain = validationHandler;
        validationHandler.setNext(authenticationHandler);
        authenticationHandler.setNext(jwtTokenHandler);
    }
    public void login(SignInDTO request) throws Exception {
        chain.handle(request);
    }
}
