package com.stanostrovskii.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Room {
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private int capacity;
	
	public Room() {} 
	
	public Room(String name, int capacity) {
		this.name = name;
		this.capacity = capacity;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", name=" + name + ", capacity=" + capacity + "]";
	}
}
