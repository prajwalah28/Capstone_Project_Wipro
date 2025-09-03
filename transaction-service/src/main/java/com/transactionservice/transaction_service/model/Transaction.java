package com.transactionservice.transaction_service.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private Long fromAccountId;
	    private Long toAccountId;

	    private Long customerId;   // reference to Customer (from Customer Service)

	    private Double amount;
	    private String type; // TRANSFER, DEBIT, CREDIT
	    private String createdBy; // username
	    private LocalDateTime createdAt;
}
