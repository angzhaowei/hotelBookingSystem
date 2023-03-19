package com.fdmgroup.hotelBookingProject.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fdmgroup.hotelBookingProject.model.Room;
import com.fdmgroup.hotelBookingProject.model.User;
import com.fdmgroup.hotelBookingProject.service.BookingService;
import com.fdmgroup.hotelBookingProject.service.RoomService;
import com.fdmgroup.hotelBookingProject.service.UserService;
import com.fdmgroup.hotelBookingProject.constants.RoomType;

import jakarta.servlet.http.HttpSession;



@Controller
//@SessionAttributes("current_user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private BookingService bookingService;
	
	Logger logger = LogManager.getLogger();


	@GetMapping("/")
	public String goToIndexPage() {
		return "index";
	}
	

	@GetMapping("/register")
	public String goToRegisterPage(Model model) {
		
		model.addAttribute("user", new User());
		
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(@RequestParam String username, @RequestParam("password") String pw) {
		
		if(userService.registerUser(username,pw)) {
			return "redirect:/login";
		}
		else {
			return "registerUserAlreadyExists";
		}

	}
	
	// this is for cases where registration fails, not just when registered user alrdy exists
	@GetMapping("/registerUserAlreadyExists")
	public String goToRegisterUserAlreadyExistsPage(Model model) {
	
		return "registerUserAlreadyExistsr";
	}
	
	@PostMapping("/registerUserAlreadyExists")
	public String registerUserAlreadyExists(User user) {
		
		if(userService.registerUser(user.getUsername(),user.getPassword())) {
			return "redirect:/login";
		}
		else {
			return "registerUserAlreadyExists";
		}

	}
	
	@GetMapping("/login")
	public String goToLogin() {
		return "login";
	}
	
	@PostMapping("/login")
	public String verifyUser(@RequestParam String username, @RequestParam("password") String pw, HttpSession session, Model model) {
		
		if(userService.verifyUser(username,pw)) {
			
			session.setAttribute("current_user", username);
//			User user = userService.findUserByUsername(username);
//			model.addAttribute("current_user", user);

			return "redirect:/userHomePage";
		}
		else {
			return "login";
		}
		
	}
	
	@GetMapping("/userHomePage")
	public String goToUserHomePage(HttpSession session, Model model) {
		
		String username = (String) session.getAttribute("current_user");
		User user = userService.findUserByUsername(username);
		model.addAttribute("current_user", user);
		
		return "userHomePage";
	}
	
	@GetMapping("/userExistingBookings")
	public String goToExistingBookingsPage(HttpSession session, Model model) {
		
		String username = (String) session.getAttribute("current_user");
		User user = userService.findUserByUsername(username);
		model.addAttribute("current_user", user);
		
		return "userExistingBookings";
	}
	
	@GetMapping("/userNewBooking")
	public String goToNewBookingPage(HttpSession session, Model model) {
		
		String username = (String) session.getAttribute("current_user");
		User user = userService.findUserByUsername(username);
		model.addAttribute("current_user", user);
		
		model.addAttribute("roomTypesList", Room.getRoomTypesAsList());
		  
		
		return "userNewBooking";
	}
	
	@PostMapping("/userNewBooking")
	public String tryToMakeBooking(@RequestParam("roomType") String roomType,
			@RequestParam("checkInDate") String checkInDate,
			@RequestParam("checkOutDate") String checkOutDate,
			HttpSession session, Model model) {
		
		//making sure the data from input is correct
		logger.info("Room Type is: "+ roomType);
		logger.info("Check in date is "+checkInDate);
		logger.info("Check out date is "+checkOutDate);
		
		// set model to session
		String username = (String) session.getAttribute("current_user");
		User user = userService.findUserByUsername(username);
		model.addAttribute("current_user", user);
		
		// needed cuz the roomType is a string
		RoomType rType = RoomType.valueOf(roomType);
		
		switch (rType) {
		case SINGLE_BED:
			if(roomService.stillHaveRooms(rType)) {
				logger.info("still have single rooms");
			}
			return "redirect:/singleBedPage";
		case DOUBLE_BED:
			return "redirect:/doubleBedPage";
		case DOUBLE_BED_BALCONY:
			return "redirect:/doubleBedBalconyPage";
		case DOUBLE_BED_BATHTUB:
			return"redirect:/doubleBedBathPage";
			default:
				return "redirect:/singleBedPage";
		}
//			if(roomType.equals()) {
//				
//				// need to do smth to check:
//				// is check in date before check out date
//				// is there enuf rooms
//				//
//				
//				return "redirect:/singleBedPage";
//			}
//			else if(roomType.equals("double_bed")) {
//				return "redirect:/doubleBedPage";
//			}
//			else if(roomType.equals("double_bed_balcony")) {
//				return "redirect:/doubleBedBalconyPage";
//			}
//			else {
//				return "redirect:/doubleBedBathPage";
//			}

	}
	
	
//	@GetMapping("/singleBedPage")
//	public String gotToSingleBedPage(HttpSession session, Model model) {
//		
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
//		
//		session.setAttribute("room_type", "single_bed");
//		
//		return "singleBedPage";
//		
//	}
//	
//	@PostMapping("/singleBedPage")
//	public String selectStartDate(@RequestParam("start_date") String startDate, HttpSession session, Model model) {
//		
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
//		
//		System.out.println(startDate);
//		
//		return "selectEndDate";
//	}
//	
//	
	
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		return "redirect:/";
	}
}
