package com.github.bilak.service;

import com.github.bilak.domain.Payment;

/**
 * @author lvasek.
 */
public interface PaymentTrackerManager {

	/**
	 * Constant for terminating the program
	 */
	String QUIT_COMMAND = "quit";

	/**
	 * Adds payment to underlying persistence unit
	 *
	 * @param payment the payment
	 */
	void addPayment(Payment payment);

	/**
	 * Terminates program if {@code command} is equal to {@link CommandLinePaymentTrackerManager#QUIT_COMMAND}
	 *
	 * @param command command received from command line
	 */
	default void quitIfNecessary(String command) {
		if (QUIT_COMMAND.equals(command)) {
			System.exit(0);
		}
	}
}
