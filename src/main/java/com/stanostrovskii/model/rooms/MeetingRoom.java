package com.stanostrovskii.model.rooms;

import javax.persistence.Entity;

@Entity
public class MeetingRoom extends Room {
	public MeetingRoom(String name, int capacity)
	{
		super(name, capacity);
	}
	public MeetingRoom() {
		super();
	}			
}
