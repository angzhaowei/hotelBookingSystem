package com.fdmgroup.hotelBookingProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.hotelBookingProject.model.ReservedDate;
import com.fdmgroup.hotelBookingProject.model.Room;


@Repository
public interface ReservedDateRepository extends JpaRepository<ReservedDate, Long>{

	public Optional<List<ReservedDate>> findAllByRoom(Room room);
}
