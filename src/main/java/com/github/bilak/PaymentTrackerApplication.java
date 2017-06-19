package com.github.bilak;

import com.github.bilak.service.CommandLinePaymentTrackerManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaymentTrackerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(PaymentTrackerApplication.class, args);
		CommandLinePaymentTrackerManager paymentTrackerManager = ctx.getBean(CommandLinePaymentTrackerManager.class);
		paymentTrackerManager.handle(args);
	}
}
