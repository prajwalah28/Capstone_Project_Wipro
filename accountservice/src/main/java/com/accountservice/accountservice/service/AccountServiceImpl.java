package com.accountservice.accountservice.service;



import org.springframework.stereotype.Service;

import com.accountservice.accountservice.feign.CustomerClient;
import com.accountservice.accountservice.model.Account;
import com.accountservice.accountservice.model.CustomerDTO;
import com.accountservice.accountservice.repo.AccountRepository;

import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final CustomerClient customerClient;

    public AccountServiceImpl(AccountRepository repository, CustomerClient customerClient) {
        this.repository = repository;
        this.customerClient = customerClient;
    }

    public Account createAccount(Account account) {
        // ✅ Validate customer from Customer Service
        // ✅ Correctly call Feign client
    	 CustomerDTO customer = (CustomerDTO) customerClient.getCustomerById(account.getCustomerId());

    	    if (customer == null || customer.getId() == null) {
    	        throw new RuntimeException("Invalid customer: " + account.getCustomerId());
    	    }

    	    return repository.save(account);
    }

    public List<Account> getAccountsByCustomer(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    public void deleteAccount(Long id) {
        repository.deleteById(id);
    }
    
    public void updateBalances(Long fromAccountId, Long toAccountId, Double amount) {
        Account from = repository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found"));
        Account to = repository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        if (from.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance in account " + fromAccountId);
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        repository.save(from);
        repository.save(to);
        
    }
}
