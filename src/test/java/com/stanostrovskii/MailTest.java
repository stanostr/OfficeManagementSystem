package com.stanostrovskii;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stanostrovskii.model.Employee;
import com.stanostrovskii.service.EmailService;
import com.stanostrovskii.util.EmployeeEmailUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {

	@Autowired
	private EmailService emailService;

	@Test
	public void mailTest() {
		EmployeeEmailUtil.sendNewEmployeeEmail(new Employee("Silky", "Ostrovskii", "silky.gajwani@yahoo.com", "868567547", "abc123"),
				emailService);
	}

}
