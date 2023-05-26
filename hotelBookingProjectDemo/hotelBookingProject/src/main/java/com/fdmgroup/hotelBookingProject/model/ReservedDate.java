package com.fdmgroup.hotelBookingProject.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReservedDate {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;
    
    @Column
    private LocalDate date;

	public ReservedDate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReservedDate(Room room, LocalDate date) {
		super();
		this.room = room;
		this.date = date;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
    

}
