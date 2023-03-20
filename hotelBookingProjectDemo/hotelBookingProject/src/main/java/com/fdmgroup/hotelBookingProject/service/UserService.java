package com.fdmgroup.hotelBookingProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.hotelBookingProject.model.User;
import com.fdmgroup.hotelBookingProject.repository.UserRepository;



@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;

	public boolean registerUser(String username, String password) {
		
		if(username.isBlank() || password.isBlank()) {
			return false;
		}
		
		Optional<User> userOptional = userRepo.findByUsername(username);
		
		if(userOptional.isEmpty()) {
			userRepo.save(new User(username, password));
			return true;
		} else {
			return false;
		}
	}
	
	public boolean loginUser(User user) {
		
		Optional<User> userOptional = userRepo.findByUsername(user.getUsername());
		
		if(!userOptional.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean verifyUser(String username, String pw) {
		
		Optional<User> userOptional = userRepo.findByUsername(username);
		if(!userOptional.isPresent()) {
			return false;
		}
		else if(userOptional.get().getPassword().equals(pw)) {
			return true;
		} else {
			return false;
		}
	}
	
	public User findUserByUsername(String username) {
		
		Optional<User> userOptional = userRepo.findByUsername(username);
		return(userOptional.get());
	}


}
