package com.example.FlightBooking.DTOs.Response;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

@Getter
@Setter
@Hidden
public class ForgotPasswordResponse {
    private Long code;
}
