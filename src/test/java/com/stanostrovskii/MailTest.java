package com.stanostrovskii;

import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.stanostrovskii.service.EmailService;
import com.stanostrovskii.util.EmployeeEmailUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {

	@Autowired
	private EmailService emailService;
	@Test
	public void mailTest() {
		EmployeeEmailUtil.sendEmail(90L, "Silky", "Ostrovskii",
				"silky.gajwani@yahoo.com", "didyoudoyourrussiantoday?", emailService);
	}

}
