package com.paymentservice.payment_service.kafka;



import com.paymentservice.payment_service.dto.PaymentEvent;
import com.paymentservice.payment_service.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "payment-topic";

    public void sendPaymentEvent(PaymentEvent event) {
        log.info("📤 Sending PaymentEvent to Kafka: {}", event);
        kafkaTemplate.send(TOPIC, event);
    }
}