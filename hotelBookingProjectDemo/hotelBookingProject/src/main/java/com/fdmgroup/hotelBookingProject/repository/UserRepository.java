package com.fdmgroup.hotelBookingProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.hotelBookingProject.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	//"Select u from User u where u.username =?"
	// find is the introducer
	// ByUsername is the criteria
	// also have read, query, get as introducers
	// can also output List<>
	// return a null value if no username, so use optional
	// no method body required, spring auto does it
	public Optional<User> findByUsername(String username);
}
