package com.accountservice.accountservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.accountservice.accountservice.model.CustomerDTO;

import java.util.List;
import java.util.Map;

@FeignClient(name = "apigateway")  // matches spring.application.name of Customer Service
public interface CustomerClient {

    @GetMapping("/api/customers/{id}")
    CustomerDTO getCustomerById(@PathVariable("id") Long id);
    
    
    @GetMapping("/api/customers/user/{userId}")
    List<Map<String, Object>> getCustomersByUserId(@PathVariable("userId") Long userId);
}
