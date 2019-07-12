package com.stanostrovskii.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class LeaveRequest {
	public static enum Status {
		APPROVED, REJECTED, PENDING, OTHER
	}
	public static enum LeaveType {
		SICK_LEAVE, VACATION_LEAVE, HOLIDAY, EARNED_LEAVE
	}
	@Id
	@GeneratedValue
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(length = 8)
	private Status status;
	@Enumerated(EnumType.STRING)
	@Column(length = 16)
	private LeaveType type;
	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "end_date")
	private Date endDate;
	private String reason;
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;
	
	public LeaveRequest() {
		status = Status.PENDING;
	}
	
	public LeaveRequest(LeaveType type, Date startDate, Date endDate, String reason, Employee employee) {
		this();
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
		this.employee = employee;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public LeaveType getType() {
		return type;
	}
	public void setType(LeaveType type) {
		this.type = type;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
