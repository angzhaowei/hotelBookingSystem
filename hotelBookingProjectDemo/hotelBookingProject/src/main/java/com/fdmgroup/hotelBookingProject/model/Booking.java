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
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date checkInDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date checkOutDate;

	public Booking() {
		super();
		// TODO Auto-generated constructor stub
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

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}


	
	
	
	
}
