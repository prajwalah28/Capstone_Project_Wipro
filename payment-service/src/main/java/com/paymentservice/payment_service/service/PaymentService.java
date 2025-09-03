package com.paymentservice.payment_service.service;


import org.springframework.stereotype.Service;

import com.paymentservice.payment_service.dto.PaymentEvent;
import com.paymentservice.payment_service.feign.AccountClient;
import com.paymentservice.payment_service.feign.CustomerClient;
import com.paymentservice.payment_service.kafka.PaymentProducer;
import com.paymentservice.payment_service.model.Payment;
import com.paymentservice.payment_service.repo.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service
public class PaymentService {

	public PaymentService(AccountClient accountclient, CustomerClient customerclient, PaymentRepository repository,
			PaymentProducer producer) {
		super();
		this.accountclient = accountclient;
		this.customerclient = customerclient;
		this.repository = repository;
		this.producer = producer;
	}

	private final AccountClient accountclient;
    private final CustomerClient customerclient;
    private final PaymentRepository repository;
    private final PaymentProducer producer;

    public Payment transfer(Long fromAccountId, Long toAccountId, Double amount,
                            String username, Long customerId, String type) {

        if (amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        String response = accountclient.transfer(fromAccountId, toAccountId, amount);
        if (!response.contains("successful")) {
            throw new RuntimeException("Balance update failed: " + response);
        }

        Map<String, Object> customer = customerclient.getCustomerById(customerId);
        if (customer == null || customer.isEmpty()) {
            throw new RuntimeException("Customer not found: " + customerId);
        }

        Object fromAccount = accountclient.getAccountsByCustomer(customerId);
        if (fromAccount == null) {
            throw new RuntimeException("Source account not found");
        }

        // ✅ Save transaction in DB
        Payment txn = Payment.builder()
                .fromAccountId(fromAccountId)
                .toAccountId(toAccountId)
                .customerId(customerId)
                .amount(amount)
                .type(type)
                .status("SUCCESS")
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();

        Payment savedTxn = repository.save(txn);

        // ✅ Create and send Kafka Event
        PaymentEvent event = new PaymentEvent(
                username + "@gmail.com",  // Replace with real user email from customer
                amount,
                savedTxn.getStatus(),
                "Transfer of " + amount + " from " + fromAccountId + " to " + toAccountId
        );

        producer.sendPaymentEvent(event);

        return savedTxn;
    }

    public List<Payment> getTransactionsForAccount(Long accountId) {
        return repository.findByFromAccountIdOrToAccountId(accountId, accountId);
    }

    public List<Payment> getAllTransactions() {
        return repository.findAll();
    }
}
