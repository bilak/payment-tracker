package com.github.bilak.repository;

import com.github.bilak.domain.Payment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author lvasek.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InMemoryPaymentRepositoryExternalFileTest.Config.class)
public class InMemoryPaymentRepositoryExternalFileTest {

	@Autowired
	private PaymentRepository paymentRepository;

	@Test
	public void repositoryShouldBePreloaded() throws URISyntaxException {
		assertTrue(paymentRepository.getActualBalance().count() > 0);
	}

	@Test
	public void eurBalanceWithInitial100EurAmountShouldBeIncreasedWith100Eur() {
		paymentRepository.addPayment(new Payment(Currency.getInstance("EUR"), new BigDecimal("100")));
		assertEquals(new BigDecimal("200"),
				paymentRepository.getActualBalance()
						.filter(p -> p.getCurrency().equals(Currency.getInstance("EUR")))
						.findFirst()
						.get()
						.getAmount());
	}

	@Configuration
	@EnableAutoConfiguration
	static class Config {

		@Bean
		PaymentRepository paymentRepository() throws URISyntaxException {
			URL balance = this.getClass().getClassLoader().getResource("balance.txt");
			return new InMemoryPaymentRepository(Paths.get(balance.toURI()));
		}
	}
}
