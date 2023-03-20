package com.fdmgroup.hotelBookingProject.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long bookingId;
	
	@ManyToOne
	private User userThatMadeBooking;
	
	@ManyToOne
	private Room roomBooked;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate checkInDate;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate checkOutDate;

	public Booking() {
		super();
	}
	
	public Booking(User userThatMadeBooking, Room roomBooked, LocalDate checkInDate, LocalDate checkOutDate) {
		super();
		this.userThatMadeBooking = userThatMadeBooking;
		this.roomBooked = roomBooked;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
	}

	public long getBookingId() {
		return bookingId;
	}

	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}

	public User getUserThatMadeBooking() {
		return userThatMadeBooking;
	}

	public void setUserThatMadeBooking(User userThatMadeBooking) {
		this.userThatMadeBooking = userThatMadeBooking;
	}


	public Room getRoomBooked() {
		return roomBooked;
	}

	public void setRoomBooked(Room roomBooked) {
		this.roomBooked = roomBooked;
	}

	public LocalDate getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	@Override
	public String toString() {
		return "userThatMadeBooking=" + userThatMadeBooking.getUsername() + ", roomBooked="
				+ roomBooked.getRoomId() + ", checkInDate=" + checkInDate + ", checkOutDate=" + checkOutDate + "]";
	}



	
	
	
	
}
