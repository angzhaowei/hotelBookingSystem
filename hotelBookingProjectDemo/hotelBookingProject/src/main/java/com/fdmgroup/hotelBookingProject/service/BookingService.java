package com.fdmgroup.hotelBookingProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.hotelBookingProject.model.Booking;
import com.fdmgroup.hotelBookingProject.repository.BookingRepository;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepo;

	public void confirmBooking(Booking booking) {
		bookingRepo.save(booking);
		
	}
	
	

}
