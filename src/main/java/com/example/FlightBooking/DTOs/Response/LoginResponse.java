package com.example.FlightBooking.DTOs.Response;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

@Getter
@Setter
@Hidden
public class LoginResponse {
    private String token;
    private long expiresIn;
}
