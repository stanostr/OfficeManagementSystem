package com.stanostrovskii.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.time.DateUtils;

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
		//for simplicity, reservations are by hour
		this.startTime = DateUtils.truncate(startTime, Calendar.HOUR);
		this.endTime = DateUtils.ceiling(startTime, Calendar.HOUR);
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
		//truncate to nearest hour
		this.startTime = DateUtils.truncate(startTime, Calendar.HOUR);
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		//rounds up to nearest hour
		this.endTime = DateUtils.ceiling(startTime, Calendar.HOUR);
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
