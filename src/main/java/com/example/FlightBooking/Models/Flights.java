package com.example.FlightBooking.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "flights")
public class Flights {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
}
