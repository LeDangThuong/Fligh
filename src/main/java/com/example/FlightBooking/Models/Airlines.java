package com.example.FlightBooking.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Generated;

@Data
@Entity
@Table(name = "airlines")
public class Airlines {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
}
