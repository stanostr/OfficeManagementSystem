package com.stanostrovskii.model;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

@Entity
public class TrainingRoom extends Room {
	@Type(type="yes_no")
	private boolean projectorAvail;
	
	@Type(type="yes_no")
	private boolean whiteboardAvail;
	
	public TrainingRoom()
	{
		super();
	}
	
	public TrainingRoom(String name, int capacity, boolean projector, boolean whiteboard)
	{
		super(name, capacity);
		this.projectorAvail = projector;
		this.whiteboardAvail = whiteboard;
	}

	public boolean isProjectorAvail() {
		return projectorAvail;
	}

	public void setProjectorAvail(boolean projectorAvail) {
		this.projectorAvail = projectorAvail;
	}

	public boolean isWhiteboardAvail() {
		return whiteboardAvail;
	}

	public void setWhiteboardAvail(boolean whiteboardAvail) {
		this.whiteboardAvail = whiteboardAvail;
	}
}
