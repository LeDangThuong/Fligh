package com.example.FlightBooking.Repositories;



import com.example.FlightBooking.Models.Regulation;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface RegulationRepository extends JpaRepository<Regulation, Long> {
    // custom query methods if needed
}
