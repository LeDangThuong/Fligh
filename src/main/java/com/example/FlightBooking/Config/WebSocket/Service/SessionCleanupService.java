package com.example.FlightBooking.Config.WebSocket.Service;

import com.example.FlightBooking.Repositories.SupportSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentMap;

@Service
public class SessionCleanupService {

    @Autowired
    private SupportSessionRepository supportSessionRepository;

    @Autowired
    private ConcurrentMap<Integer, Long> chatEndTimes;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupSessions() {
        long currentTime = Instant.now().getEpochSecond();

        chatEndTimes.entrySet().removeIf(entry -> {
            if (currentTime - entry.getValue() > 7200) { // 2 hours
                Integer sessionId = entry.getKey();
                supportSessionRepository.deleteById(Long.valueOf(sessionId));
                return true;
            }
            return false;
        });
    }
}
