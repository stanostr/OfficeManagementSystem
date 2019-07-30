package com.stanostrovskii.model.rooms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.stanostrovskii.model.Employee;

@Entity
public class RoomReservation {
	public enum Status {
		APPROVED, REJECTED, PENDING, OTHER
	}

	@Id
	@GeneratedValue
	@Column(name = "reservation_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time")
	private Date startTime;
	@Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;
	@Enumerated(EnumType.STRING)
	@Column(length = 8)
	private Status status;

	public RoomReservation() {
		this.startTime = new Date();
		this.endTime = new Date();
		status = Status.PENDING;
	}

	public RoomReservation(Employee employee, Date startTime, Date endTime) {
		this.employee = employee;
		this.startTime = startTime;
		this.endTime = endTime;
		status = Status.PENDING;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setEmployee(Employee employee)
	{
		this.employee = employee;
	}
	public Employee getEmployee() {
		return employee;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
