package com.fdmgroup.hotelBookingProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.hotelBookingProject.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{

}
