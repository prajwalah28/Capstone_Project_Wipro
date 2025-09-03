package com.transactionservice.transaction_service.service;


import org.springframework.stereotype.Service;

import com.transactionservice.transaction_service.feign.AccountClient;
import com.transactionservice.transaction_service.feign.CustomerClient;
import com.transactionservice.transaction_service.feign.NotificationClient;
import com.transactionservice.transaction_service.feign.NotificationRequest;
import com.transactionservice.transaction_service.model.Transaction;
import com.transactionservice.transaction_service.repo.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final CustomerClient customerclient;
   
    private final AccountClient accountclient;
    private final NotificationClient notificationclient;

    // ✅ Inject all dependencies properly
    public TransactionService(TransactionRepository repository,
            CustomerClient customerclient,
            AccountClient accountclient,
            NotificationClient notificationclient) {   // include here
this.repository = repository;
this.customerclient = customerclient;
this.accountclient = accountclient;
this.notificationclient = notificationclient;   // Spring injects automatically
}

    public Transaction transfer(Long fromAccountId, Long toAccountId, Double amount, String username, Long customerId) {
        if (amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        String response = accountclient.transfer(fromAccountId, toAccountId, amount);
        if (!response.contains("successful")) {
            throw new RuntimeException("Balance update failed: " + response);
        }
        // ✅ Validate Customer
        Map<String, Object> customer = customerclient.getCustomerById(customerId);
        if (customer == null || customer.isEmpty()) {
            throw new RuntimeException("Customer not found: " + customerId);
        }

        // ✅ Validate Accounts
        Object fromAccount = accountclient.getAccountsByCustomer(customerId);
        if (fromAccount == null) {
            throw new RuntimeException("Source account not found");
        }

        // Save transaction
        Transaction txn = Transaction.builder()
                .fromAccountId(fromAccountId)
                .toAccountId(toAccountId)
                .customerId(customerId)
                .amount(amount)
                .type("TRANSFER")
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();

        Map<String, Object> customerMap = customerclient.getCustomerById(customerId);
        String email = (String) customer.get("email");   // must match field name from Customer Service

        // ✅ Send success notification
        notificationclient.sendNotification(NotificationRequest.builder()
                .toEmail(email)
                .subject("Payment Successful")
                .message("Dear " + customer.get("name") +
                         ", your payment of Rs " + amount + " was successful.")
                .build()
        );
        return repository.save(txn);
    }

    public List<Transaction> getTransactionsForAccount(Long accountId) {
        return repository.findByFromAccountIdOrToAccountId(accountId, accountId);
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }
}
