package com.github.bilak.repository;

import com.github.bilak.domain.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Repository holding {@link Payment} in {@link ConcurrentLinkedQueue}. If path to payments balance source file is
 * provided through constructor, current balance will be written to this file after each new incoming payment.
 *
 * @author lvasek.
 */
public class InMemoryPaymentRepository implements PaymentRepository {

	private static final Logger logger = LoggerFactory.getLogger(InMemoryPaymentRepository.class);
	/**
	 * Name of parameter for balance file
	 */
	public static final String BALANCE_FILE_PARAM = "balance_file";

	private Optional<Path> paymentBalanceSource;
	private Queue<Payment> payments = new ConcurrentLinkedQueue<>();

	public InMemoryPaymentRepository(Path paymentBalanceSource) {
		this.paymentBalanceSource = Optional.ofNullable(paymentBalanceSource);
	}

	@PostConstruct
	protected void init() {
		paymentBalanceSource.ifPresent(source -> {
			if (source.toFile().exists()) {
				try {
					Files.readAllLines(source)
							.stream()
							.map(line -> line.trim().split(" "))
							.filter(balance -> balance.length == 2)
							.map(balance -> new Payment(Currency.getInstance(balance[0]), new BigDecimal(balance[1])))
							.forEach(payments::add);
				} catch (Exception e) {
					logger.error("Unable to read balance from file {}. Application will not use it.", source.toString());
				}
			} else {
				logger.error("Specified balance file {} does not exists", source.toString());
			}
		});
	}

	@Override
	public void addPayment(Payment payment) {
		payments.add(payment);
	}

	@Override
	public Stream<Payment> getActualBalance() {
		return reduceBalance(
				payments
						.stream()
						.collect(Collectors.groupingBy(Payment::getCurrency)));
	}

	private Stream<Payment> reduceBalance(Map<Currency, List<Payment>> payments) {
		return payments
				.entrySet()
				.stream()
				.map(entry ->
						new Payment(
								entry.getKey(),
								entry.getValue()
										.stream()
										.map(mt -> mt.getAmount())
										.reduce(BigDecimal.ZERO, BigDecimal::add)));
	}

}
