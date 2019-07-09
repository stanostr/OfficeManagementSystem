package com.stanostrovskii.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class RoomReservation {
	@Id
	@GeneratedValue
	@Column(name = "reservation_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="room_id")
	private Room room;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time")
	private Date startTime;
	@Column(name="end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;
	
	public RoomReservation() {}
	public RoomReservation(Date startTime, Date endTime)
	{
		this.startTime = startTime;
		this.endTime = endTime;
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
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
