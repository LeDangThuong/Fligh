package com.example.FlightBooking.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles, Long> {
    Roles findByName (String name);
}
