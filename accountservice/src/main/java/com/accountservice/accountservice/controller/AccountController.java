
package com.accountservice.accountservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.accountservice.accountservice.feign.CustomerClient;
import com.accountservice.accountservice.model.Account;
import com.accountservice.accountservice.model.CustomerDTO;
import com.accountservice.accountservice.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;
    private final CustomerClient customerClient;

    public AccountController(AccountService service, CustomerClient customerClient) {
        this.service = service;
        this.customerClient = customerClient; // ✅ injected properly
    }

    // ADMIN: Create account
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account, HttpServletRequest request) {
        String role = request.getHeader("X-Auth-Role");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(service.createAccount(account));
    }

    // USER: View their accounts
    @GetMapping("/my")
    public ResponseEntity<?> getMyAccounts(HttpServletRequest request) {
        String role = request.getHeader("X-Auth-Role");
        Long userId = Long.valueOf(request.getHeader("X-Auth-UserId"));

        if (!"USER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        // ✅ Fetch all customers for this userId
        List<Map<String, Object>> customers = customerClient.getCustomersByUserId(userId);

        if (customers == null || customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No customers found for this user");
        }

        // ✅ Collect accounts for each customer
        List<Account> accounts = new ArrayList<>();
        for (Map<String, Object> cust : customers) {
            Long customerId = ((Number) cust.get("id")).longValue();
            List<Account> customerAccounts = (List<Account>) service.getAccountsByCustomer(customerId);
            if (customerAccounts != null) {
                accounts.addAll(customerAccounts);
            }
        }

        return ResponseEntity.ok(accounts);
    }




    // ADMIN: View all accounts
    @GetMapping
    public ResponseEntity<?> getAllAccounts(HttpServletRequest request) {
        String role = request.getHeader("X-Auth-Role");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(service.getAllAccounts());
    }

    // ADMIN: Delete account
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, HttpServletRequest request) {
        String role = request.getHeader("X-Auth-Role");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        service.deleteAccount(id);
        return ResponseEntity.ok("Account deleted");
    }
    
    @GetMapping("/customer/{customerId}")
    public List<Account> getAccountsByCustomer(@PathVariable Long customerId) {
        return (List<Account>) service.getAccountsByCustomer(customerId);
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam Long fromAccountId,
                                      @RequestParam Long toAccountId,
                                      @RequestParam Double amount) {
        try {
            service.updateBalances(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}

