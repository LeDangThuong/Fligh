package com.example.FlightBooking.Repositories;


import com.example.FlightBooking.Models.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource
public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByUsername(String username);
    Optional<Users> findByUsername(String username);

}


