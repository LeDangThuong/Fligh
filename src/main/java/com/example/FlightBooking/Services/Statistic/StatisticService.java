package com.example.FlightBooking.Services.Statistic;

import com.example.FlightBooking.Repositories.BookingRepository;
import com.example.FlightBooking.Repositories.FlightRepository;
import com.example.FlightBooking.Repositories.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class StatisticService {
    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Double getTotalRevenue() {
        return statisticsRepository.getTotalRevenue();
    }

    public Double getTotalRevenueByDateRange(Timestamp startDate, Timestamp endDate) {
        return statisticsRepository.getTotalRevenueByDateRange(startDate, endDate);
    }
    public Long countFlightsByDateRange(Timestamp startDate, Timestamp endDate) {
        return flightRepository.countFlightsByDateRange(startDate, endDate);
    }

    public Long countBookingsByDateRange(Timestamp startDate, Timestamp endDate) {
        return bookingRepository.countBookingsByDateRange(startDate, endDate);
    }

}
