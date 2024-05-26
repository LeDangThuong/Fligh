package com.example.FlightBooking.Repositories.Decorator;

import com.example.FlightBooking.Models.Decorator.Vouchers;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface VoucherRepository extends JpaRepository<Vouchers, Long> {
}
