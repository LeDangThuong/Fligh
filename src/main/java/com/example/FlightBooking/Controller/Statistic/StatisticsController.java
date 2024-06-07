package com.example.FlightBooking.Controller.Statistic;

import com.example.FlightBooking.Services.Statistic.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/statistic")
public class StatisticsController {
    @Autowired
    private StatisticService statisticsService;

    @GetMapping("/revenue")
    public Double getTotalRevenue() {
        return statisticsService.getTotalRevenue();
    }

    @GetMapping("/revenueByDateRange")
    public Double getTotalRevenueByDateRange(@RequestParam("startDate") Timestamp startDate, @RequestParam("endDate") Timestamp endDate) {
        return statisticsService.getTotalRevenueByDateRange(startDate, endDate);
    }
}
