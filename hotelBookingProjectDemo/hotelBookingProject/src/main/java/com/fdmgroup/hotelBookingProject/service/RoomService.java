package com.fdmgroup.hotelBookingProject.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.hotelBookingProject.repository.ReservedDateRepository;
import com.fdmgroup.hotelBookingProject.repository.RoomRepository;
import com.fdmgroup.hotelBookingProject.repository.UserRepository;
import com.fdmgroup.hotelBookingProject.model.Booking;
import com.fdmgroup.hotelBookingProject.model.ReservedDate;
import com.fdmgroup.hotelBookingProject.model.Room;

import com.fdmgroup.hotelBookingProject.constants.RoomType;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepo;
	
	@Autowired
	private ReservedDateRepository reservedDateRepo;
	
	@Autowired
	private DateService dateService;
	
	public Room getRoomById(long id) {
		Optional<Room> room = roomRepo.findById(id);
		return room.get();
	}

//	public boolean stillHaveRooms(RoomType roomType) {
//
//		// dont need optional, will just output empty list if dun have
//		//Optional<List<Room>> roomsOptional = roomRepo.findListByRoomType(roomType);
//		List<Room> roomsList = roomRepo.findAllByRoomType(roomType);
//
//		if (roomsList.isEmpty()) {
//			return false;
//		} else return true;
//		
//	}

	public List<Room> getRoomsListByRoomType(RoomType roomType){
		
		List<Room> roomsList = roomRepo.findAllByRoomType(roomType);
		return roomsList;
	}
	
	public boolean atLeastOneRoomWithDatesAvailable(RoomType roomType, String startDate, String endDate) {
		
		// get all rooms of the type first
		List<Room> roomsList = roomRepo.findAllByRoomType(roomType);
		ArrayList<Room> roomsArrayList = (ArrayList<Room>) roomsList;
		
		// convert dates to LocalDate
		LocalDate checkInDate = dateService.convertFromStringToLocalDate(startDate);
		LocalDate checkOutDate = dateService.convertFromStringToLocalDate(endDate);
		
		// get the desired days
		ArrayList<LocalDate> desiredDates = dateService.getAllDatesWithin(checkInDate, checkOutDate);
		
		// for each room, check that got no clashes
		int roomsAvailable = 0;
		for(Room eachRoom: roomsArrayList) {
			List<ReservedDate> eachRoomDates = eachRoom.getReservedDates();
			if(!dateService.checkForDatesClash(desiredDates,eachRoomDates)) {
				// need the ! because true means date clash, room isnt available on those dates
				roomsAvailable ++;
			}
		}
		
		if(roomsAvailable>=1) {
			return true;
		} else return false;
	}
	
	



	// basically the same as the checking, but now return the first room
	public Room getRoomWithAvailableDates(RoomType roomType, String startDate, String endDate) {
		
		// get all rooms of the type first
		List<Room> roomsList = roomRepo.findAllByRoomType(roomType);
		ArrayList<Room> roomsArrayList = (ArrayList<Room>) roomsList;
				
		// convert dates to LocalDate
		LocalDate checkInDate = dateService.convertFromStringToLocalDate(startDate);
		LocalDate checkOutDate = dateService.convertFromStringToLocalDate(endDate);
				
		// get the desired days
		ArrayList<LocalDate> desiredDates = dateService.getAllDatesWithin(checkInDate, checkOutDate);
				
		// for each room, check that got no clashes
		Room chosenRoom = null;
		for(Room eachRoom: roomsArrayList) {
			List<ReservedDate> eachRoomDates = eachRoom.getReservedDates();
				if(!dateService.checkForDatesClash(desiredDates,eachRoomDates)) {
				// need the ! because true means date clash, room isnt available on those dates
				chosenRoom = eachRoom;
				break;
			}
		}
		return chosenRoom;

	}
	
	public void confirmReservedDates(Booking booking) {
		
		List<LocalDate> bookedDates = dateService.getAllDatesWithin(booking.getCheckInDate(), booking.getCheckOutDate());

	    for (LocalDate date : bookedDates) {
	        ReservedDate rDate = new ReservedDate(booking.getRoomBooked(), date);
	        reservedDateRepo.save(rDate);
	    }
	    
		
	}
	
}
