package com.stanostrovskii.util;

import java.text.SimpleDateFormat;

import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.LeaveRequest;
import com.stanostrovskii.model.Task;
import com.stanostrovskii.model.rooms.RoomReservation;
import com.stanostrovskii.service.EmailService;

/**
 * Util to send e-mail messages to employees and admins.
 * @author Stan
 */
public class EmployeeEmailUtil {
	public static void sendNewEmployeeEmail(Employee employee, EmailService emailService) {
		String subject = "Welcome on board " + employee.getFirstName() + " " + employee.getLastName();
		String message = "Welcome to E-office Corp. " + employee.getFirstName() + ", your details are below: "
				+ "\n\n Employee ID: "+ employee.getId() + "\nEmployee login: " + employee.getEmail() + "\nPassword: " + 
				employee.getPassword() + "\n\n" + "Regards, \nAdmin Team \nE-Office Corp.";
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
	
	public static void sendReservationRejectionEmail(RoomReservation reservation, EmailService emailService)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		Employee employee = reservation.getEmployee();
		String employeeEmail = employee.getEmail();
		String subject = "E-Office Corp: Reservation Request for room rejected";
		String message = "Dear " + employee.getFirstName() + ",\n" +
				"An admin has rejected your reservation request.\nReservation Details:\nRoom: " + reservation.getRoom().getName() +
				"\nDate: "+ dateFormat.format(reservation.getStartTime()) +
				"\nFrom: "+ timeFormat.format(reservation.getStartTime()) +
				"\nTo: "+ timeFormat.format(reservation.getEndTime()) +
				"\nStatus: REJECTED"+
				"\nPlease contact us if you have any questions.\n\nRegards, \n Admin Team \n E-Office Corp.";
		emailService.sendSimpleMessage(employeeEmail, subject, message);
	}
	
	public static void sendReservationApprovedEmail(RoomReservation reservation, EmailService emailService)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		Employee employee = reservation.getEmployee();
		String employeeEmail = reservation.getEmployee().getEmail();
		String subject = "E-Office Corp: Reservation Request for room approved";
		String message = "Dear " + employee.getFirstName() + ",\n" +
				"An admin has approved your reservation request.\nReservation Details:\nRoom: " + reservation.getRoom().getName() +
				"\nDate: "+ dateFormat.format(reservation.getStartTime()) +
				"\nFrom: "+ timeFormat.format(reservation.getStartTime()) +
				"\nTo: "+ timeFormat.format(reservation.getEndTime()) +
				"\nStatus: APPROVED"+
				"\nPlease contact us if you have any further questions.\n\nRegards, \n Admin Team \n E-Office Corp.";
		emailService.sendSimpleMessage(employeeEmail, subject, message);
	}

	public static void sendLeaveApprovedEmail(LeaveRequest request, EmailService emailService) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		Employee employee = request.getEmployee();
		String employeeEmail = request.getEmployee().getEmail();
		String subject = "E-Office Corp: Leave Request approved";
		String message = "Dear " + employee.getFirstName() + ",\n" +
				"An admin has approved your leave request.\nRequest Details:\nLeave type: " + request.getType() +
				"\nStart Date: "+ dateFormat.format(request.getStartDate()) +
				"\nEnd Date: "+ dateFormat.format(request.getEndDate()) +
				"\nReason: "+ request.getReason()  +
				"\nStatus: APPROVED"+
				"\n\nRegards, \n Admin Team \n E-Office Corp.";
		emailService.sendSimpleMessage(employeeEmail, subject, message);
	}

	public static void sendLeaveRejectionEmail(LeaveRequest request, EmailService emailService) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		Employee employee = request.getEmployee();
		String employeeEmail = request.getEmployee().getEmail();
		String subject = "E-Office Corp: Leave Request declined";
		String message = "Dear " + employee.getFirstName() + ",\n" +
				"An admin has declined your leave request.\nRequest Details:\nLeave type: " + request.getType() +
				"\nStart Date: "+ dateFormat.format(request.getStartDate()) +
				"\nEnd Date: "+ dateFormat.format(request.getEndDate()) +
				"\nReason: "+ request.getReason()  +
				"\nStatus: DECLINED"+
				"\nPlease contact us if you have any further questions.\n\nRegards, \n Admin Team \n E-Office Corp.";
		emailService.sendSimpleMessage(employeeEmail, subject, message);		
	}
}
