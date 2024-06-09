package com.example.FlightBooking.DTOs.Request.Flight;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Data
public class FlightDTO {
    private String flightStatus;
    private Timestamp departureDate;
    private Timestamp arrivalDate;

    private Long duration;
    private Long departureAirportId;
    private Long arrivalAirportId;
    private Long planeId;

    private Double economyPrice;
    private Double businessPrice;
    private Double firstClassPrice;

    private String setSeatStatuses;
    private String popularPlaceUrl;
}
