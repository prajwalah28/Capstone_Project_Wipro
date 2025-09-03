package com.transactionservice.transaction_service.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "customerservice")
public interface CustomerClient {
	   @GetMapping("/api/customers")
	    List<Map<String,Object>> getAllCustomers();

	    @GetMapping("/api/customers/{id}")
	    Map<String,Object> getCustomerById(@PathVariable("id") Long id);

	    @PostMapping("/api/customers")
	    Map<String,Object> saveCustomer(@RequestBody Map<String,Object> customer);

	    @DeleteMapping("/api/customers/{id}")
	    void deleteCustomer(@PathVariable("id") Long id);

	    @GetMapping("/api/customers/user/{userId}")
	    List<Map<String,Object>> getCustomersByUserId(@PathVariable("userId") Long userId);
}
