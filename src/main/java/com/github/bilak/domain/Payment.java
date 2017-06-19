package com.github.bilak.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import static java.lang.String.format;

/**
 * Payment domain object holding the {@link Currency} and {@link BigDecimal amount} of payment.
 *
 * @author lvasek.
 */
public class Payment {

	private final Currency currency;
	private final BigDecimal amount;

	public Payment(Currency currency, BigDecimal amount) {
		this.currency = currency;
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Creates payment from string representation. String should consist of currency in ISO 4217, blank space and decimal value
	 * e.g <code>EUR 123.44</code>.
	 *
	 * @param payment
	 * @return payment with currency and amount
	 * @throws IllegalArgumentException if currency does not exists or if bad decimal format is provided
	 */
	public static Payment from(String payment) {
		Objects.requireNonNull(payment, "Payment can not be null");
		String[] paymentData = payment.split(" ");
		if (paymentData.length != 2)
			throw new IllegalArgumentException("Payment should consist of currency and amount (example: EUR 1234.33)");
		Currency currency = Currency.getAvailableCurrencies()
				.stream()
				.filter(c -> paymentData[0].equals(c.getCurrencyCode()))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(format("Currency [%s] does not exists", paymentData[0])));
		BigDecimal amount;
		try {
			amount = new BigDecimal(paymentData[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(format("Bad format for decimal value [%s]", paymentData[1]));
		}
		return new Payment(currency, amount);
	}

	@Override
	public String toString() {
		return "Payment{" +
				"currency=" + currency +
				", amount=" + amount +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(currency, amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Payment other = (Payment) obj;
		return Objects.equals(this.currency, other.currency)
				&& Objects.equals(this.amount, other.amount);
	}

}
