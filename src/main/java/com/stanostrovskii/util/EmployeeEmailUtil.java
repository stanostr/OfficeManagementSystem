package com.stanostrovskii.util;

import java.text.SimpleDateFormat;

import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.Task;
import com.stanostrovskii.service.EmailService;

/**
 * Util to send e-mail message to newly registered employees.
 * @author Stan
 */
public class EmployeeEmailUtil {
	public static void sendNewEmployeeEmail(Employee employee, EmailService emailService) {
		String subject = "Welcome on board " + employee.getFirstName() + " " + employee.getLastName();
		String message = "Welcome to E-office Corp. " + employee.getFirstName() + ", your details are below: "
				+ "\n\n Employee ID: "+ employee.getId() + "\n Employee login: " + employee.getEmail() + "\n Password: " + 
				employee.getPassword() + "\n\n" + "Regards, \n Admin Team \n E-Office Corp.";
		emailService.sendSimpleMessage(employee.getEmail(), subject, message);
	}
	
	public static void sendNewTaskEmail(Employee employee, Task task, EmailService emailService) { 
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/YYYY");
		String subject = "E-Office Corp: Task Assignment Notification";
		String message = "Hi " + employee.getFirstName() + " " + employee.getLastName() +
				",\nYou have been assigned a new task" + 
				"\nTask name: " + task.getTaskName() +
				"\nDescription: " + task.getDescription() + 
				"\nDate assigned: " + format.format(task.getStartDate()) + 
				"\nDue date: " + format.format(task.getDueDate()) +
				"\nPlease complete before the due date. \n\n" 
				+ "Regards, \n Admin Team \n E-Office Corp.";
		emailService.sendSimpleMessage(employee.getEmail(), subject, message);
	} 
	
	public static void sendCompletedTaskEmail(Task task, EmailService emailService)
	{
		String adminEmail = task.getAdmin().getEmail(); 
		String subject = "E-Office Corp: An employee has completed a task";
		String message = "Task with task ID " + task.getId() + " has been marked completed by " +
				task.getEmployee().getFirstName() + " " + task.getEmployee().getLastName() + "\n" +
				"Please check the website for details.";
		emailService.sendSimpleMessage(adminEmail, subject, message);
	}
}
