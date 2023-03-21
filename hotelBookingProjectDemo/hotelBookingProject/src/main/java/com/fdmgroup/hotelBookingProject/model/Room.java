package com.fdmgroup.hotelBookingProject.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fdmgroup.hotelBookingProject.constants.RoomType;
import com.fdmgroup.hotelBookingProject.service.DateService;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Room {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long roomId;
	
	private RoomType roomType;
	
	//private ArrayList<LocalDate> reservedDates = new ArrayList<>();
	
	//@OneToMany(mappedBy="room")
    //private List<LocalDate> reservedDates = new ArrayList<>();
	
	//@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
	@OneToMany(mappedBy = "room")
	@Fetch(FetchMode.JOIN)
	private List<ReservedDate> reservedDates = new ArrayList<>();

	
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

//	public List<LocalDate> getReservedDates() {
//		return reservedDates;
//	}

	public List<ReservedDate> getReservedDates() {
		return reservedDates;
	}


	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
	

	public static ArrayList<String> getRoomTypesAsList(){
		ArrayList<String> roomTypes = new ArrayList<String>();
		for (RoomType roomType: RoomType.values()) {
			roomTypes.add(roomType.toString());
		
		}
		return roomTypes;
		
	}
	
	
//	public void addToRoomReservedDatesList(Booking booking) {		
//		
//		LocalDate start = booking.getCheckInDate();
//		LocalDate end = booking.getCheckOutDate();
//		
//		DateService dateService = new DateService();
//		
//		ArrayList<LocalDate> datesWithin = dateService.getAllDatesWithin(start, end);
//		
//		for (LocalDate date: datesWithin) {
//			reservedDates.add(date);
//		}
//		
//	}
	
	
	public void addToRoomReservedDatesList(Booking booking) {
	    
		DateService dateService = new DateService();
		
		List<LocalDate> bookedDates = dateService.getAllDatesWithin(booking.getCheckInDate(), booking.getCheckOutDate());

	    for (LocalDate date : bookedDates) {
	        reservedDates.add(new ReservedDate(this, date));
	    }
	    
	}
	
}
