package com.accountservice.accountservice.service;

import com.accountservice.accountservice.model.Account;

public interface AccountService {

	Object createAccount(Account account);

	Object getAccountsByCustomer(Long customerId);

	Object getAllAccounts();

	void deleteAccount(Long id);

	void updateBalances(Long fromAccountId, Long toAccountId, Double amount);

}
