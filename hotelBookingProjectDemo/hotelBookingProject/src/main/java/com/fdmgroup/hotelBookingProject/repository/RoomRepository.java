package com.fdmgroup.hotelBookingProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.hotelBookingProject.constants.RoomType;
import com.fdmgroup.hotelBookingProject.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	public List<Room> findAllByRoomType(RoomType roomType);


}
