package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Message;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findTopBySenderIdAndReceiverIdOrderByCreatedAtDesc(Long senderId, Long receiverId);
}
