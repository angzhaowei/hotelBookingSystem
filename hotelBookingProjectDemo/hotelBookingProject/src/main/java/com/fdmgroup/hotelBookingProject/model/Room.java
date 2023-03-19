package com.fdmgroup.hotelBookingProject.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fdmgroup.hotelBookingProject.constants.RoomType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Room {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long roomId;
	
//	public enum RoomType{
//		SINGLE_BED,
//		DOUBLE_BED,
//		DOUBLE_BED_BALCONY,
//		DOUBLE_BED_BATHTUB;
//		
//	};
	
	private RoomType roomType;
	
	private ArrayList<Date> reservedDates;

	
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

	public ArrayList<Date> getReservedDates() {
		return reservedDates;
	}

	public void setReservedDates(ArrayList<Date> reservedDates) {
		this.reservedDates = reservedDates;
	}

	public RoomType getRoomType() {
		return roomType;
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
	
}
