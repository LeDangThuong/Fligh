package com.example.FlightBooking.Config.WebSocket;

import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Models.SupportSession;
import com.example.FlightBooking.Repositories.MessageRepository;
import com.example.FlightBooking.Repositories.SupportSessionRepository;
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

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SupportSessionRepository supportSessionRepository;

    @Autowired
    private ConcurrentMap<Integer, Long> chatEndTimes;

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

        if (receivedMessage.getContent() == null || receivedMessage.getContent().isEmpty()) {
            throw new RuntimeException("Message content cannot be null or empty");
        }

        receivedMessage.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        SupportSession supportSession = supportSessionRepository.findByCustomerIdAndStatus(receivedMessage.getSenderId(), "pending");

        if (supportSession == null) {
            supportSession = new SupportSession();
            supportSession.setCustomerId(receivedMessage.getSenderId());
            supportSession.setStatus("pending");
            supportSession.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            supportSession = supportSessionRepository.save(supportSession);
        } else if (supportSession.getStatus().equals("closed")) {
            supportSession.setStatus("pending");
            supportSession.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            supportSessionRepository.save(supportSession);
        }

        receivedMessage.setSessionId(supportSession.getId());
        messageRepository.save(receivedMessage);

        for (WebSocketSession sess : sessions) {
            if (sess.isOpen()) {
                sess.sendMessage(new TextMessage(payload));
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
        log.info("Connection closed on session: {} with status: {}", session.getId(), closeStatus.getCode());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}