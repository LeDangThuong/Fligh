package com.example.FlightBooking.Controller.SignIn;

import com.example.FlightBooking.DTOs.Request.ApiResponse;
import com.example.FlightBooking.DTOs.Request.AuthenticationRequest;
import com.example.FlightBooking.DTOs.Request.IntrospectRequest;
import com.example.FlightBooking.DTOs.Response.AuthenticationResponse;
import com.example.FlightBooking.DTOs.Response.IntrospectResponse;
import com.example.FlightBooking.Services.AuthenticationService;
import com.example.FlightBooking.Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SignInController {
    @Autowired
    private UserService userService;
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
