package com.fdmgroup.hotelBookingProject.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.hotelBookingProject.repository.RoomRepository;
import com.fdmgroup.hotelBookingProject.repository.UserRepository;
import com.fdmgroup.hotelBookingProject.model.Room;

import com.fdmgroup.hotelBookingProject.constants.RoomType;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepo;
	
	@Autowired
	private DateService dateService;
	
	public Room getRoomById(long id) {
		Optional<Room> room = roomRepo.findById(id);
		return room.get();
	}

	public boolean stillHaveRooms(RoomType roomType) {

		// dont need optional, will just output empty list if dun have
		//Optional<List<Room>> roomsOptional = roomRepo.findListByRoomType(roomType);
		List<Room> roomsList = roomRepo.findAllByRoomType(roomType);

		if (roomsList.isEmpty()) {
			return false;
		} else return true;
		
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
		
		// predicate for stream filter
		Predicate<Room> roomIsFreeOnDates = (room) -> {
			Optional<ArrayList<LocalDate>>reservedDates = Optional.ofNullable(room.getReservedDates());
			for (LocalDate date: desiredDates) {
				if (reservedDates.isPresent() && reservedDates.get().contains(date)) {
					return false;
				}
			}
			return true;
		};
		
		ArrayList<Long> roomsListAvailable = (ArrayList<Long>) roomsArrayList.stream()
				.filter(roomIsFreeOnDates)
				.map(room -> room.getRoomId())
				.collect(Collectors.toList());
		
		if(roomsListAvailable.isEmpty()) {
			return false;
		} else return true;

	}
	
	public List<Room> getRoomsListByRoomType(RoomType roomType){
		
		List<Room> roomsList = roomRepo.findAllByRoomType(roomType);
		return roomsList;
	}

	// basically the same as the checking, but now returns the first room
	public Room getRoomWithAvailableDates(RoomType roomType, String startDate, String endDate) {
		
		// get all rooms of the type first
		List<Room> roomsList = roomRepo.findAllByRoomType(roomType);
		ArrayList<Room> roomsArrayList = (ArrayList<Room>) roomsList;
		
		// convert dates to LocalDate
		LocalDate checkInDate = dateService.convertFromStringToLocalDate(startDate);
		LocalDate checkOutDate = dateService.convertFromStringToLocalDate(endDate);
		
		// get the desired days
		ArrayList<LocalDate> desiredDates = dateService.getAllDatesWithin(checkInDate, checkOutDate);
		
		// predicate for stream filter
		Predicate<Room> roomIsFreeOnDates = (room) -> {
			Optional<ArrayList<LocalDate>>reservedDates = Optional.ofNullable(room.getReservedDates());
			for (LocalDate date: desiredDates) {
				if (reservedDates.isPresent() && reservedDates.get().contains(date)) {
					return false;
				}
			}
			return true;
		};
		
		ArrayList<Long> roomsListAvailable = (ArrayList<Long>) roomsArrayList.stream()
				.filter(roomIsFreeOnDates)
				.map(room -> room.getRoomId())
				.collect(Collectors.toList());		
		
		Room room = this.getRoomById(roomsListAvailable.get(0));
		
		return room;
	}
	
}
