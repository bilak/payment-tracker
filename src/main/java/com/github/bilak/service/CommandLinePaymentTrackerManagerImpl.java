package com.github.bilak.service;

import com.github.bilak.domain.Payment;
import com.github.bilak.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Scanner;

/**
 * Implementation of {@link CommandLinePaymentTrackerManager}
 *
 * @author lvasek.
 */
public class CommandLinePaymentTrackerManagerImpl implements CommandLinePaymentTrackerManager {

	private static final Logger logger = LoggerFactory.getLogger(CommandLinePaymentTrackerManagerImpl.class);

	private PaymentRepository paymentRepository;

	public CommandLinePaymentTrackerManagerImpl(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	@Override
	public void handle(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			String line;
			while (true) {
				line = scanner.nextLine();
				if (!StringUtils.isEmpty(line)) {
					quitIfNecessary(line);
					try {
						addPayment(Payment.from(line));
					} catch (Exception e) {
						logger.error("Error while adding payment {}", e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public void addPayment(Payment payment) {
		paymentRepository.addPayment(payment);
	}

}
