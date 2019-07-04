package com.stanostrovskii.util;

import com.stanostrovskii.service.EmailService;

/**
 * Util to send e-mail message to newly registered employees.
 * @author Stan
 */
public class EmployeeEmailUtil {
	public static void sendEmail(Long id, String firstName, String lastName, String email, String password, EmailService emailService) {
		String subject = "Welcome on board " + firstName + " " + lastName;
		String message = "Welcome to E-office Corp. " + firstName + ", your details are below: "
				+ "\n\n Employee ID: "+ id + "\n Employee login: " + email + "\n Password: " + 
				password + "\n\n" + "Regards, \n Admin Team \n E-Office Corp.";
		emailService.sendSimpleMessage(email, subject, message);
	}
}
