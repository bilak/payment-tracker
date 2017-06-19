package com.github.bilak.task;

import com.github.bilak.domain.ExchangeRate;
import com.github.bilak.domain.Payment;
import com.github.bilak.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

/**
 * Implementation of {@link BalanceCheckTask} which logs current balance to SLF4J logger.
 * Task is executed periodically specified by property <code>application.task.balancePrinter.cron</code> or defaults to
 * each minute.
 *
 * @author lvasek.
 */
public class LoggingBalanceCheckTask implements BalanceCheckTask {

	private static final Logger logger = LoggerFactory.getLogger(LoggingBalanceCheckTask.class);

	private PaymentRepository paymentRepository;
	private Resource exchangeRateResource;
	private Currency exchangeRateCurrency;
	private Map<Currency, List<ExchangeRate>> exchangeRates = Collections.emptyMap();

	public LoggingBalanceCheckTask(PaymentRepository paymentRepository, Resource exchangeRateResource, Currency exchangeRateCurrency) {
		Objects.requireNonNull(paymentRepository, "Payment repository is required");
		Objects.requireNonNull(exchangeRateResource, "Exchange rate resource is required");
		this.paymentRepository = paymentRepository;
		this.exchangeRateResource = exchangeRateResource;
		this.exchangeRateCurrency = exchangeRateCurrency == null ? Currency.getInstance("USD") : exchangeRateCurrency;
	}

	@PostConstruct
	protected void init() {
		if (exchangeRateResource.exists() && exchangeRateResource.isReadable())
			try {
				exchangeRates =
						Files.readAllLines(Paths.get(exchangeRateResource.getURI()))
								.stream()
								.map(line -> line.split(" "))
								.filter(lineArray -> lineArray.length == 2)
								.map(lineArray -> {
									String[] currencies = lineArray[0].split("/");
									if (currencies.length == 2) {
										return Optional.of(new ExchangeRate(
												Currency.getInstance(currencies[1]),
												Currency.getInstance(currencies[0]),
												new BigDecimal(lineArray[1])));
									}
									return Optional.<ExchangeRate>empty();
								})
								.filter(er -> er.isPresent())
								.map(Optional::get)
								.collect(groupingBy(ExchangeRate::getTarget));
			} catch (IOException e) {
				logger.error("Unable to load exchange rates, balance check wont include them");
			}

	}

	@Override
	@Scheduled(cron = "${application.task.balancePrinter.cron:0 * * * * *}")
	public void checkBalance() {
		paymentRepository.getActualBalance()
				.filter(payment -> payment.getAmount().compareTo(BigDecimal.ZERO) != 0)
				.forEach(payment -> logBalanceWithExchangeRate(payment));
	}

	/**
	 * Logs current {@code currency} balance with exchange rate if found
	 *
	 * @param payment current payment
	 */
	private void logBalanceWithExchangeRate(Payment payment) {
		Optional<ExchangeRate> exchangeRate = Optional.ofNullable(exchangeRates.get(exchangeRateCurrency))
				.map(rates -> rates.stream().filter(ex -> ex.getSource().equals(payment.getCurrency())).findFirst())
				.filter(rate -> rate.isPresent())
				.map(Optional::get);

		if (exchangeRate.isPresent()) {
			exchangeRate.ifPresent(er -> {
				BigDecimal computed =
						payment.getAmount().compareTo(BigDecimal.ONE) > 0 ?
								payment.getAmount().divide(er.getRate(), 2, BigDecimal.ROUND_HALF_UP) :
								payment.getAmount().multiply(er.getRate());
				logger.info(payment.getCurrency() + " " + payment.getAmount() + " (" + er.getTarget() + " " + computed + ")");
			});
		} else {
			logger.info(payment.getCurrency() + " " + payment.getAmount());
		}
	}

}
