package com.stanostrovskii.model.rooms;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class EmployeeRoomReservationRequest {
	private Long roomId;
	private Date startTime;
	private Date endTime;
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = DateUtils.truncate(startTime, Calendar.HOUR);
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = DateUtils.ceiling(endTime, Calendar.HOUR);
	}
	
}
