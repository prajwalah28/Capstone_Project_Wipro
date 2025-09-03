package com.CustomerService.customerservice.controller;

import java.util.List;
import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CustomerService.customerservice.entity.Customer;
import com.CustomerService.customerservice.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired 
    private CustomerService service;

    // ✅ Only ADMIN can see all customers
    @GetMapping
    public List<Customer> getAllCustomers(@RequestHeader("X-Auth-Role") String role) {
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access Denied: Only ADMIN can view all customers");
        }
        return service.getAllCustomers();
    }

    // ✅ USER can view own profile, ADMIN can view any
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(
            @PathVariable Long id,
            @RequestHeader(value = "X-Auth-Username", required = false) String username,
            @RequestHeader(value = "X-Auth-Role", required = false) String role) {

        Customer customer = service.getCustomerById(id);

        if (customer == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                                 .body(Map.of("error", "Customer not found with id " + id));
        }

        // ✅ If Feign call (no headers)
        if (role == null && username == null) {
            return ResponseEntity.ok(customer);
        }

        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(customer);
        } else if (username != null && username.equals(customer.getEmail())) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.SC_FORBIDDEN)
                                 .body(Map.of("error", "Access denied"));
        }
    }



    // ✅ Only USER can create customer profile
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer,
                                   @RequestHeader("X-Auth-Username") String username,
                                   @RequestHeader("X-Auth-Role") String role,
                                   @RequestHeader(value = "X-Auth-UserId", required = false) Long userId) {
        if (!"USER".equals(role)) {
            throw new RuntimeException("Access Denied: Only USER can create customer profile");
        }

        if (userId == null) {
            throw new RuntimeException("Missing userId in token");
        }

        customer.setUserId(userId);
        return service.saveCustomer(customer);
    }




    // ✅ Only ADMIN can delete
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id,
                               @RequestHeader("X-Auth-Role") String role) {
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access Denied: Only ADMIN can delete customers");
        }
        service.deleteCustomer(id);
    }
    
    @GetMapping("/user/{userId}")
    public List<Customer> getCustomersByUserId(@PathVariable Long userId) {
        return service.getCustomersByUserId(userId);
    }
}
