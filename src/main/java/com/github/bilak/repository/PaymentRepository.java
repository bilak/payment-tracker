package com.github.bilak.repository;

import com.github.bilak.domain.Payment;

import java.util.stream.Stream;

/**
 * Repository for {@link Payment}
 *
 * @author lvasek.
 */
public interface PaymentRepository {

	/**
	 * Add new {@link Payment} record
	 *
	 * @param payment the payment to add
	 */
	void addPayment(Payment payment);

	/**
	 * Retrieves actual balance of payments
	 *
	 * @return {@link Stream} of {@link Payment} for individual currencies
	 */
	Stream<Payment> getActualBalance();

}
