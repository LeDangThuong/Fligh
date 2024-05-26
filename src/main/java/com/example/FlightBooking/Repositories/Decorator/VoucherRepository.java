package com.example.FlightBooking.Repositories.Decorator;

import com.example.FlightBooking.Models.Decorator.Vouchers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Vouchers, Long> {
}
