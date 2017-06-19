package com.github.bilak.task;

/**
 * Task performing balance check
 *
 * @author lvasek.
 */
public interface BalanceCheckTask {

	/**
	 * Name of parameter for exchange rate file
	 */
	String EXCHANGE_FILE_PARAM = "exchange_file";
	/**
	 * Default name of file in which exchange rates are saved
	 */
	String DEFAULT_EXCHANGE_RATE_FILE = "exchange_rates.txt";

	/**
	 * Checks current balance
	 */
	void checkBalance();
}
