package com.example.FlightBooking.Controller.Auth.ForgotPassword;

import com.example.FlightBooking.DTOs.Request.Auth.ResetPasswordDTO;
import com.example.FlightBooking.Services.AuthJWT.AuthenticationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@CrossOrigin
@Tag(name = "Authentication", description = "APIs for authenticate for user")
public class ResetPasswordController {
    @Autowired
    private AuthenticationService authenticationService;


    @PutMapping("/auth/reset-password")
    public ResponseEntity<String> ResetPassword(@RequestParam ResetPasswordDTO resetPasswordDTO){
        return new ResponseEntity<>(authenticationService.reset_password(resetPasswordDTO.getEmail(), resetPasswordDTO.getOtp(), resetPasswordDTO.getNewPassword(), resetPasswordDTO.getConfirmPassword()), HttpStatus.OK);
    }

}
