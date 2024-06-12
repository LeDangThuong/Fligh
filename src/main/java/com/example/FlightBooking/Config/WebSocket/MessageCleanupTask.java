package com.example.FlightBooking.Config.WebSocket;

import com.example.FlightBooking.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class MessageCleanupTask {
    @Autowired
    private MessageRepository messageRepository;

    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void cleanupOldMessages() {
        Timestamp cutoffTime = Timestamp.valueOf(LocalDateTime.now().minusHours(2));
        messageRepository.deleteByTimeSupportBefore(cutoffTime);
    }
}
