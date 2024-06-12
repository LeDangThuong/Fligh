package com.example.FlightBooking.Config.WebSocket;

import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Repositories.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class TutorialHandler implements WebSocketHandler {

    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final ConcurrentMap<Integer, WebSocketSession> customerSessions = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, WebSocketSession> adminSessions = new ConcurrentHashMap<>();

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("Connection established on session: {}", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();
        log.info("Message received: {}", payload);

        ObjectMapper objectMapper = new ObjectMapper();
        Message receivedMessage = objectMapper.readValue(payload, Message.class);

        // Identify message type and sender
        if ("JOIN".equals(receivedMessage.getType())) {
            customerSessions.put(receivedMessage.getSenderId(), session);
            log.info("Customer {} joined with session {}", receivedMessage.getSenderId(), session.getId());
            return;
        } else if ("JOIN_ADMIN".equals(receivedMessage.getType())) {
            adminSessions.put(receivedMessage.getSenderId(), session);
            log.info("Admin {} joined with session {}", receivedMessage.getSenderId(), session.getId());
            return;
        }

        receivedMessage.setTimeSupport(Timestamp.valueOf(LocalDateTime.now()));
        messageRepository.save(receivedMessage);

        if (receivedMessage.getReceiverId() == null) {
            // Broadcast to all admins
            for (WebSocketSession adminSession : adminSessions.values()) {
                if (adminSession.isOpen()) {
                    adminSession.sendMessage(new TextMessage(payload));
                }
            }
        } else {
            // Send to specific admin and customer
            WebSocketSession customerSession = customerSessions.get(receivedMessage.getSenderId());
            WebSocketSession adminSession = adminSessions.get(receivedMessage.getReceiverId());

            if (customerSession != null && customerSession.isOpen()) {
                customerSession.sendMessage(new TextMessage(payload));
            }

            if (adminSession != null && adminSession.isOpen()) {
                adminSession.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Exception occurred: {} on session: {}", exception.getMessage(), session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
        customerSessions.values().remove(session);
        adminSessions.values().remove(session);
        log.info("Connection closed on session: {} with status: {}", session.getId(), closeStatus.getCode());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
