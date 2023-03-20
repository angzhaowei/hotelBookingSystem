package com.fdmgroup.hotelBookingProject.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fdmgroup.hotelBookingProject.constants.RoomType;
import com.fdmgroup.hotelBookingProject.service.DateService;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
public class Room {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long roomId;
	
	private RoomType roomType;
	
//	@ElementCollection
//	@CollectionTable(name="room_reserved_dates", joinColumns= {@JoinColumn(name = "room_id")})
	private ArrayList<LocalDate> reservedDates = new ArrayList<>();

	
	public Room() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getRoomId() {
		return roomId;
	}

//	public void setRoomId(long roomId) {
//		this.roomId = roomId;
//	}
	

	public RoomType getRoomType() {
		return roomType;
	}

	public ArrayList<LocalDate> getReservedDates() {
		return reservedDates;
	}

	public void setReservedDates(ArrayList<LocalDate> reservedDates) {
		this.reservedDates = reservedDates;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
	
	public static List<String> getRoomTypesAsList(){
		List<String> roomTypes = new ArrayList<String>();
		for (RoomType roomType: RoomType.values()) {
			roomTypes.add(roomType.toString());
		
		}
		return roomTypes;
		
	}
	
	
	public void addToRoomReservedDatesList(Booking booking) {		
		
		LocalDate start = booking.getCheckInDate();
		LocalDate end = booking.getCheckOutDate();
		
		DateService dateService = new DateService();
		
		ArrayList<LocalDate> datesWithin = dateService.getAllDatesWithin(start, end);
		
		for (LocalDate date: datesWithin) {
			reservedDates.add(date);
		}
		
;	}
	
}
