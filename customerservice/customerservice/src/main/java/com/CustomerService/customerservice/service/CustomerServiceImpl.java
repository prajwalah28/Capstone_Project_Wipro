package com.CustomerService.customerservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CustomerService.customerservice.entity.Customer;
import com.CustomerService.customerservice.repo.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
 
	   @Autowired CustomerRepository repository;

	   

	    public CustomerServiceImpl(CustomerRepository repository) {
		super();
		this.repository = repository;
	}


	    
	    public List<Customer> getAllCustomers() {
	        return repository.findAll();
	    }

	
	    public Customer getCustomerById(Long id) {
	        return repository.findById(id).orElse(null);
	    }

	   
	    public Customer saveCustomer(Customer customer) {
	        return repository.save(customer);
	    }

	   
	    public void deleteCustomer(Long id) {
	        repository.deleteById(id);
	    }



		@Override
		public List<Customer> getCustomersByUserId(Long userId) {
			// TODO Auto-generated method stub
			return repository.findByUserId(userId);
		}
}
