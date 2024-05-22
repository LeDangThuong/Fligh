package com.example.FlightBooking.Models;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "flights")
// Cai model nay la luu thong tin CHUYEN BAY
public class Flights {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightStatus; // Cai nay la dang delay, hay hoan, hay la san sang cat canh
    private String flightType; // 1 chieu, khu hoi, hay nhieu chang

    private Timestamp departureDate; //thoi gian cat canh
    private Timestamp arrivalDate; // thoi gian ha canh
    private Long duration; // uoc luong thoi gian bay
    private int economySeatsAvailable;
    private int businessSeatsAvailable;
    private int firstClassSeatsAvailable;
    // Luu noi cat canh va ha canh de khi search de search, cung nhu luu thong tin chuyen bay
    private Long departureAirportId;
    private Long arrivalAirportId;

    private Long airlineId; // Luu thong tin may bay cua hang nao

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
