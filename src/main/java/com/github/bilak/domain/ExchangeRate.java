package com.github.bilak.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Exchange rate domain object holding information about currencies and their exchange rate
 *
 * @author lvasek.
 */
public class ExchangeRate {
	/**
	 * Source currency
	 */
	private Currency source;
	/**
	 * Target currency
	 */
	private Currency target;
	/**
	 * Currencies exchange rate
	 */
	private BigDecimal rate;

	public ExchangeRate(Currency source, Currency target, BigDecimal rate) {
		this.source = source;
		this.target = target;
		this.rate = rate;
	}

	public Currency getSource() {
		return source;
	}

	public Currency getTarget() {
		return target;
	}

	public BigDecimal getRate() {
		return rate;
	}

	@Override
	public String toString() {
		return "ExchangeRate{" +
				"source=" + source +
				", target=" + target +
				", rate=" + rate +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, target, rate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final ExchangeRate other = (ExchangeRate) obj;
		return Objects.equals(this.source, other.source)
				&& Objects.equals(this.target, other.target)
				&& Objects.equals(this.rate, other.rate);
	}
}
