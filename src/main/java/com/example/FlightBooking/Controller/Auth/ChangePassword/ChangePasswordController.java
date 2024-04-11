package com.example.FlightBooking.Controller.Auth.ChangePassword;

import com.example.FlightBooking.DTOs.Request.Auth.ForgotPasswordDTO;
import com.example.FlightBooking.Services.AuthJWT.AuthenticationService;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@CrossOrigin(value = {"http://localhost:7050", "https://flightbookingbe-production.up.railway.app"})
public class ChangePasswordController {
    @Autowired
    private AuthenticationService authenticationService;
    @PutMapping("/auth/change-password")
    public ResponseEntity<String> ChangePassword(@ModelAttribute ForgotPasswordDTO forgotPasswordDTO){
        return new ResponseEntity<>(authenticationService.change_password(forgotPasswordDTO.getEmail(), forgotPasswordDTO.getOldPassword(), forgotPasswordDTO.getNewPassword()), HttpStatus.OK);
    }
}
