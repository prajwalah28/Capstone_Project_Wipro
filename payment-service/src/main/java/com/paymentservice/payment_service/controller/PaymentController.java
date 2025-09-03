package com.paymentservice.payment_service.controller;




import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paymentservice.payment_service.model.Payment;
import com.paymentservice.payment_service.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:4200") 
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // User can transfer between their accounts
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam Long fromAccountId,
                                      @RequestParam Long toAccountId,
                                      @RequestParam Double amount,
                                      @RequestParam String type,
                                      @RequestParam Long customerId,
                                      HttpServletRequest request) {
        String role = request.getHeader("X-Auth-Role");
        String username = request.getHeader("X-Auth-Username");
        
        System.out.println("🚀 Received X-Auth-Role header: [" + role + "]"); 
        System.out.println("🚀 Received role header: " + role); 
        if (!"USER".equals(role) && !"ROLE_USER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only users can perform transfers");
        }

        Payment txn = service.transfer(fromAccountId, toAccountId, amount, username, customerId,type);
        return ResponseEntity.ok(txn);
    }
    // User can see their transactions
    @GetMapping("/my/{accountId}")
    public ResponseEntity<?> getMyTransactions(@PathVariable Long accountId, HttpServletRequest request) {
        String role = request.getHeader("X-Auth-Role");

        if (!"USER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(service.getTransactionsForAccount(accountId));
    }

    // Admin can see all transactions
    @GetMapping
    public ResponseEntity<?> getAllTransactions(HttpServletRequest request) {
        String role = request.getHeader("X-Auth-Role");

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin only access");
        }
        return ResponseEntity.ok(service.getAllTransactions());
    }
}
