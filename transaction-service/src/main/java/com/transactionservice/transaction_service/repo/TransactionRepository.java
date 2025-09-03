package com.transactionservice.transaction_service.repo;



import org.springframework.data.jpa.repository.JpaRepository;

import com.transactionservice.transaction_service.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findByFromAccountIdOrToAccountId(Long fromAccountId, Long toAccountId);
}
