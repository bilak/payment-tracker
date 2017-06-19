package com.github.bilak.service;

/**
 * Payment tracker manager which manges inputs from command line interface
 *
 * @author lvasek.
 */
public interface CommandLinePaymentTrackerManager extends PaymentTrackerManager {

	/**
	 * Handles all user inputs
	 *
	 * @param args arguments provided through command line. Each line is considered as one command.
	 */
	void handle(String[] args);
}
