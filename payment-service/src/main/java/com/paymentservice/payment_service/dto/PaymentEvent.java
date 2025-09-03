package com.paymentservice.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {
    private String userEmail;
    private double amount;
    private String status;  // SUCCESS or FAILED
    private String message;
}
