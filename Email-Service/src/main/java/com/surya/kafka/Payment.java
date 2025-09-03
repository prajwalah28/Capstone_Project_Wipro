package com.surya.kafka;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String userEmail;
    private double amount;
    private String status;  // SUCCESS or FAILED
    private String message;
}
