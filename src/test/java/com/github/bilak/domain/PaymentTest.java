package com.github.bilak.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

/**
 * @author lvasek.
 */
public class PaymentTest {

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();


	@Test
	public void testPaymentParse(){
		Payment p = Payment.from("USD 124.55");
		assertEquals(new Payment(Currency.getInstance("USD"), new BigDecimal("124.55")), p);
	}

	@Test
	public void testPaymentParseThrowsExceptionWhenBadCurrency(){
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Currency [XYZ] does not exists");
		Payment p = Payment.from("XYZ 123.44");
	}

	@Test
	public void testPaymentParseThrowsExceptionWhenBadAmount(){
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Bad format for decimal value [123xyz]");
		Payment p = Payment.from("EUR 123xyz");
	}

	@Test
	public void testPaymentParseThrowsExceptionWhenInputHasNotTwoStrings(){
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Payment should consist of currency and amount (example: EUR 1234.33)");
		Payment p = Payment.from("EUR123xyz");
	}
}