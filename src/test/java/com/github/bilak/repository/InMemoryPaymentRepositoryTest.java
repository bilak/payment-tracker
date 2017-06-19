package com.github.bilak.repository;

import com.github.bilak.domain.Payment;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * @author lvasek.
 */
public class InMemoryPaymentRepositoryTest {

	private PaymentRepository paymentRepository = new InMemoryPaymentRepository(null);

	@Test
	public void testBalance() {
		paymentRepository.addPayment(new Payment(Currency.getInstance("USD"), new BigDecimal("100")));
		paymentRepository.addPayment(new Payment(Currency.getInstance("USD"), new BigDecimal("200")));
		paymentRepository.addPayment(new Payment(Currency.getInstance("USD"), new BigDecimal("300")));
		paymentRepository.addPayment(new Payment(Currency.getInstance("USD"), new BigDecimal("400")));
		paymentRepository.addPayment(new Payment(Currency.getInstance("EUR"), new BigDecimal("400")));
		paymentRepository.addPayment(new Payment(Currency.getInstance("EUR"), new BigDecimal("400")));
		paymentRepository.addPayment(new Payment(Currency.getInstance("EUR"), new BigDecimal("400")));

		Map<Currency, BigDecimal> balance =
				paymentRepository.getActualBalance()
						.collect(Collectors.toMap(Payment::getCurrency, Payment::getAmount));

		assertEquals(2, balance.size());
		assertEquals(new BigDecimal("1000"), balance.get(Currency.getInstance("USD")));
		assertEquals(new BigDecimal("1200"), balance.get(Currency.getInstance("EUR")));
	}

}