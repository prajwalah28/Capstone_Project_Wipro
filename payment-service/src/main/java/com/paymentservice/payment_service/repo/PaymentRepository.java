package com.paymentservice.payment_service.repo;



import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentservice.payment_service.model.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByFromAccountIdOrToAccountId(Long fromAccountId, Long toAccountId);
}
