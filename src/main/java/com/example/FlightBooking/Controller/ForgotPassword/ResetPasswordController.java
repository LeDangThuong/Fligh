package com.example.FlightBooking.Controller.ForgotPassword;

import com.example.FlightBooking.DTOs.Request.ForgotPasswordDTO;
import com.example.FlightBooking.DTOs.Request.ResetPasswordDTO;
import com.example.FlightBooking.Services.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
public class ResetPasswordController {
    @Autowired
    private AuthenticationService authenticationService;
    @PutMapping("/auth/reset-password")
    public ResponseEntity<String> ResetPassword(@ModelAttribute ResetPasswordDTO resetPasswordDTO){
        return new ResponseEntity<>(authenticationService.reset_password(resetPasswordDTO.getEmail(), resetPasswordDTO.getOtp(), resetPasswordDTO.getNewPassword(), resetPasswordDTO.getConfirmPassword()), HttpStatus.OK);
    }

}
