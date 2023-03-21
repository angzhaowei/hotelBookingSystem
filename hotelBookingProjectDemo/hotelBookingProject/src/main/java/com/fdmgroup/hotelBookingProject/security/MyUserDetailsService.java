package com.fdmgroup.hotelBookingProject.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fdmgroup.hotelBookingProject.model.User;
import com.fdmgroup.hotelBookingProject.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> userOptional = userRepo.findByUsername(username);
		
		User user = userOptional.orElseThrow(()
				-> new UsernameNotFoundException("User with username "+ username +" not found"));
		
		return new UserPrincipal(user);
		
	}
	
	// spring security needs 2 things:
	// user principal, and user details service

}
