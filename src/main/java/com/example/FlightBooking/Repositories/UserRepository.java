package com.example.FlightBooking.Repositories;


import com.example.FlightBooking.Models.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import io.swagger.v3.oas.annotations.Hidden;
import springfox.documentation.annotations.ApiIgnore;

@Repository
@RepositoryRestResource
@Hidden
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail (String email);
}


