package com.github.bilak.configuration;

import com.github.bilak.repository.InMemoryPaymentRepository;
import com.github.bilak.repository.PaymentRepository;
import com.github.bilak.service.CommandLinePaymentTrackerManager;
import com.github.bilak.service.CommandLinePaymentTrackerManagerImpl;
import com.github.bilak.task.BalanceCheckTask;
import com.github.bilak.task.LoggingBalanceCheckTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Paths;
import java.util.Currency;
import java.util.Optional;

/**
 * @author lvasek.
 */
@Configuration
public class PaymentTrackerConfiguration {

	private Environment environment;

	public PaymentTrackerConfiguration(Environment environment) {
		this.environment = environment;
	}

	@Bean
	PaymentRepository paymentRepository() {
		return Optional
				.ofNullable(environment.getProperty(InMemoryPaymentRepository.BALANCE_FILE_PARAM))
				.map(file -> new InMemoryPaymentRepository(Paths.get(file)))
				.orElseGet(() -> new InMemoryPaymentRepository(null));
	}

	@Bean
	CommandLinePaymentTrackerManager commandLinePaymentTrackerManager() {
		return new CommandLinePaymentTrackerManagerImpl(paymentRepository());
	}

	@Bean
	BalanceCheckTask balancePrinterTask() {
		Currency exchangeRateCurrency = Currency.getInstance("USD");
		return Optional
				.ofNullable(environment.getProperty(BalanceCheckTask.EXCHANGE_FILE_PARAM))
				.map(file -> new LoggingBalanceCheckTask(paymentRepository(), new FileSystemResource(file), exchangeRateCurrency))
				.orElseGet(() -> new LoggingBalanceCheckTask(paymentRepository(), new ClassPathResource(BalanceCheckTask.DEFAULT_EXCHANGE_RATE_FILE),
						exchangeRateCurrency));
	}
}
