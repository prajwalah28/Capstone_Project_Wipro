package com.surya.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPaymentEmail(Payment payment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(payment.getUserEmail());
        message.setSubject("Payment Status - " + payment.getStatus());
        message.setText(payment.getMessage() + "\n\nAmount: " + payment.getAmount());

        mailSender.send(message);
    log.warn(" Email sent to : {} " , payment.getUserEmail());
    }
}
