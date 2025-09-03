package com.surya.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic", groupId = "email-group")
    public void consume(Payment payment) {
        log.warn("✅ Received Payment: {}", payment);
        emailService.sendPaymentEmail(payment);
    }
}