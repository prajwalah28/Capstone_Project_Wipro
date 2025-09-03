package com.paymentservice.payment_service.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "accountservice")
public interface AccountClient {
	  @GetMapping("/accounts/customer/{customerId}")
	    Object getAccountsByCustomer(@PathVariable("customerId") Long customerId);

	    @GetMapping("/accounts")
	    Object getAllAccounts();

	    @PostMapping("/accounts")
	    Object createAccount(@RequestBody Map<String, Object> account);

	    @DeleteMapping("/accounts/{id}")
	    void deleteAccount(@PathVariable("id") Long id);
	    
	    
	    @PostMapping("/accounts/transfer")
	    String transfer(@RequestParam Long fromAccountId,
	                    @RequestParam Long toAccountId,
	                    @RequestParam Double amount);
	    
	    
}
