package com.fdmgroup.hotelBookingProject.service;

import java.util.List;
import java.util.Optional;

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

	public boolean stillHaveRooms(RoomType roomType) {
		
		Optional<List<Room>> roomsOptional = roomRepo.findByRoomType(roomType);
		
		if (roomsOptional.get().isEmpty()) {
			return false;
		} else return true;
		
	}

	
	
}
