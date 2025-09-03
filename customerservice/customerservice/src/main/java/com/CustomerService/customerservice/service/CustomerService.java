package com.CustomerService.customerservice.service;

import java.util.List;

import com.CustomerService.customerservice.entity.Customer;

public interface CustomerService {

	List<Customer> getAllCustomers();

	Customer getCustomerById(Long id);

	Customer saveCustomer(Customer customer);

	void deleteCustomer(Long id);

	List<Customer> getCustomersByUserId(Long userId);



}
