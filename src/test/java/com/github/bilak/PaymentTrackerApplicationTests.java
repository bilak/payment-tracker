package com.github.bilak;

import com.github.bilak.repository.PaymentRepository;
import com.github.bilak.service.CommandLinePaymentTrackerManager;
import com.github.bilak.task.BalanceCheckTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "balance_file=./balance.txt")
public class PaymentTrackerApplicationTests {

	@Autowired
	ApplicationContext appContext;

	@Test
	public void contextLoads() {
		assertNotNull(appContext.getBean(PaymentRepository.class));
		assertNotNull(appContext.getBean(CommandLinePaymentTrackerManager.class));
		assertNotNull(appContext.getBean(BalanceCheckTask.class));
	}

}
