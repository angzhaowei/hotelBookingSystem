package com.fdmgroup.hotelBookingProject.initialiser;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.fdmgroup.hotelBookingProject.constants.RoomType;
import com.fdmgroup.hotelBookingProject.model.Booking;
import com.fdmgroup.hotelBookingProject.model.Room;
import com.fdmgroup.hotelBookingProject.model.User;
import com.fdmgroup.hotelBookingProject.repository.BookingRepository;
import com.fdmgroup.hotelBookingProject.repository.RoomRepository;
import com.fdmgroup.hotelBookingProject.repository.UserRepository;
import com.fdmgroup.hotelBookingProject.service.DateService;
import com.fdmgroup.hotelBookingProject.service.RoomService;
import com.fdmgroup.hotelBookingProject.service.UserService;

import jakarta.annotation.PostConstruct;

@Component
public class Initialiser {

	@Autowired
	private RoomRepository roomRepo;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
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
		
		// create this user everytime it reruns
		User user1 = new User("user1",passwordEncoder.encode("pw1"));
		userRepo.save(user1);

		
		// create these bookings everytime it reruns
		User user = userService.findUserByUsername("user1");

		ArrayList<Room> rooms = (ArrayList<Room>) roomService.getRoomsListByRoomType(RoomType.SINGLE_BED);
				
		LocalDate checkInDate = LocalDate.of(2023, 03, 18);
		LocalDate checkOutDate = LocalDate.of(2023, 03, 20);
				
		// got 5 rooms, create booking for 5 rooms on these dates!
		for (int i =0; i<5; i++) {
			Booking booking = new Booking(user,rooms.get(i), checkInDate, checkOutDate);
			user.addBooking(booking);
			bookingRepo.save(booking);
			rooms.get(i).addToRoomReservedDatesList(booking);
			roomService.confirmReservedDates(booking);
		}
    }
}
