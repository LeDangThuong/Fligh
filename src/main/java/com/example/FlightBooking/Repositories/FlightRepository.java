package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Flights;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RepositoryRestResource
@Hidden
public interface FlightRepository extends JpaRepository<Flights, Long> {

    @Query("SELECT f FROM Flights f WHERE f.departureAirportId = :departureAirportId AND f.arrivalAirportId = :arrivalAirportId AND f.departureDate = :departureDate")
    List<Flights> searchFlightOneWay(
            @Param("departureAirportId") Long departureAirportId,
            @Param("arrivalAirportId") Long arrivalAirportId,
            @Param("departureDate") Timestamp departureDate
    );

    @Query("SELECT f FROM Flights f WHERE f.departureAirportId = :departureAirportId AND f.arrivalAirportId = :arrivalAirportId AND f.departureDate BETWEEN :departureStartDate AND :departureEndDate")
    List<Flights> searchFlightRoundTrip(
            @Param("departureAirportId") Long departureAirportId,
            @Param("arrivalAirportId") Long arrivalAirportId,
            @Param("departureStartDate") Timestamp departureStartDate,
            @Param("departureEndDate") Timestamp departureEndDate
    );

    @Query("SELECT f FROM Flights f WHERE f.departureAirportId = :departureAirportId AND f.arrivalAirportId = :arrivalAirportId AND f.departureDate BETWEEN :departureStartDate AND :departureEndDate")
    List<Flights> searchFlightMulti(
            @Param("departureAirportId") Long departureAirportId,
            @Param("arrivalAirportId") Long arrivalAirportId,
            @Param("departureStartDate") Timestamp departureStartDate,
            @Param("departureEndDate") Timestamp departureEndDate
    );
}
