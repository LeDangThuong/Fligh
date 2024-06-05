package com.example.FlightBooking.Repositories;



import com.example.FlightBooking.Models.Regulation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegulationRepository extends JpaRepository<Regulation, Long> {
    // custom query methods if needed
}
