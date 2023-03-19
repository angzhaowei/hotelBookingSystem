package com.fdmgroup.hotelBookingProject.initialiser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.hotelBookingProject.constants.RoomType;
import com.fdmgroup.hotelBookingProject.model.Room;
import com.fdmgroup.hotelBookingProject.repository.RoomRepository;

import jakarta.annotation.PostConstruct;

@Component
public class RoomInitialiser {

	@Autowired
	private RoomRepository roomRepo;
	
	@PostConstruct
    public void init() {
		
		// create 5 rooms for each roomType
		for(RoomType rType: RoomType.values()) {
			
			for(int i=0; i<5; i++) {
				Room room = new Room();
				room.setRoomType(rType);
				roomRepo.save(room);
			}
			
		}

    }
}
