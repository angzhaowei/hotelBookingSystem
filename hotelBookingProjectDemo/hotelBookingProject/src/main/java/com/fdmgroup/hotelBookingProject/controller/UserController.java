package com.fdmgroup.hotelBookingProject.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fdmgroup.hotelBookingProject.model.Booking;
import com.fdmgroup.hotelBookingProject.model.Room;
import com.fdmgroup.hotelBookingProject.model.User;
import com.fdmgroup.hotelBookingProject.service.BookingService;
import com.fdmgroup.hotelBookingProject.service.DateService;
import com.fdmgroup.hotelBookingProject.service.RoomService;
import com.fdmgroup.hotelBookingProject.service.UserService;
import com.fdmgroup.hotelBookingProject.constants.RoomType;

import jakarta.servlet.http.HttpSession;



@Controller
@SessionAttributes("current_user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private DateService dateService;
	
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
	
//	@PostMapping("/login")
//	public String verifyUser(@RequestParam String username, 
//			@RequestParam("password") String pw, 
//			HttpSession session, Model model) {
//		
//		if(userService.verifyUser(username,pw)) {
//			
//			session.setAttribute("current_user", username);
//
//			return "redirect:/userHomePage";
//		}
//		else {
//			return "redirect:/loginErrorTryAgain";
//		}
//		
//	}
	
	// cases where login fails
	@GetMapping("/loginErrorTryAgain")
	public String goToLoginErrorTryAgain() {
		return "loginErrorTryAgain";
	}
	
	@GetMapping("/userHomePage")
	public String goToUserHomePage(Principal principal, HttpSession session, Model model) {
		
		//String username = (String) session.getAttribute("current_user");
		String username = principal.getName();
		//session.setAttribute("current_user", username);
		User user = userService.findUserByUsername(username);
		model.addAttribute("current_user", user);
		
		
//		model.addAttribute("current_userObject", principal);
//		model.addAttribute("current_user", principal.getName());
		
		return "userHomePage";
	}
	
//	@GetMapping("/userHomePage")
//	public String goToUserHomePage(@AuthenticationPrincipal Principal principal) {
//		
//		principal.getName();
//		
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
//		
//		return "userHomePage";
//	}
	
	
	@GetMapping("/userExistingBookings")
	public String goToExistingBookingsPage(HttpSession session, Model model) {
		
		//String username = (String) session.getAttribute("current_user");
		//User user = userService.findUserByUsername(username);
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		return "userExistingBookings";
	}
	
	@GetMapping("/userNewBooking")
	public String goToNewBookingPage(HttpSession session, Model model) {
		
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
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
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
		model.addAttribute("current_user", session.getAttribute("current_user"));
		User user = (User) session.getAttribute("current_user");
		
		// date validity check
		if(checkInDate.isBlank() || checkOutDate.isBlank()) {
			return"redirect:/userNewBookingAgainEmptyDates";
		}
		else if (!dateService.checkIfStartDateBeforeEndDate(checkInDate, checkOutDate)) {
			return "redirect:/userNewBookingAgainWrongDates";
		}
		
		// needed cuz the roomType is a string
		RoomType rType = RoomType.valueOf(roomType);
		
		// room availability check
		
		if(roomService.atLeastOneRoomWithDatesAvailable(rType, checkInDate, checkInDate)) {
			
			Room room = roomService.getRoomWithAvailableDates(rType, checkInDate, checkOutDate);
			logger.info("room chosen: " + room.getRoomId());
			logger.info("Room reservedDates: "+ room.getReservedDates().toString());
			LocalDate checkIn = dateService.convertFromStringToLocalDate(checkInDate);
			LocalDate checkOut = dateService.convertFromStringToLocalDate(checkOutDate);
			Booking booking = new Booking(user, room, checkIn,checkOut);
			
			logger.info("There is at least one room available for: "+rType);
			logger.info("Booking tentative for:"+booking.toString());
			
			session.setAttribute("current_roomType", roomType);
			session.setAttribute("current_checkIn", checkInDate);
			session.setAttribute("current_checkOut", checkOutDate);
			session.setAttribute("room", room);
			
			return "redirect:/bookingConfirmationPage";
		} 
		else {
			return "redirect:/userNewBookingAgainDatesUnavailable";
		}
		

	}
	
	
	@GetMapping("/userNewBookingAgainEmptyDates")
	public String goToChooseBookingAgainEmptyDates(HttpSession session, Model model) {
		
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		model.addAttribute("roomTypesList", Room.getRoomTypesAsList());
		
		return"/userNewBookingAgainEmptyDates";
	}
	
	@GetMapping("/userNewBookingAgainWrongDates")
	public String goToChooseBookingAgainWrongDates(HttpSession session, Model model) {
		
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		model.addAttribute("roomTypesList", Room.getRoomTypesAsList());
		
		return"/userNewBookingAgainWrongDates";
	}
	
	@GetMapping("/userNewBookingAgainDatesUnavailable")
	public String goToChooseBookingAgainDatesUnavailable(HttpSession session, Model model) {
		
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		model.addAttribute("roomTypesList", Room.getRoomTypesAsList());
		
		return"/userNewBookingAgainDatesUnavailable";
	}
	
	@GetMapping("/bookingConfirmationPage")
	public String goToConfirmationPage(HttpSession session, Model model) {
		
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
		
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		String rType = (String) session.getAttribute("current_roomType");
		model.addAttribute("current_roomType", rType);
		
		String checkInDate = (String) session.getAttribute("current_checkIn");
		model.addAttribute("current_checkInDate", checkInDate);
		
		String checkOutDate = (String) session.getAttribute("current_checkOut");
		model.addAttribute("current_checkOutDate", checkOutDate);
		
		return "/bookingConfirmationPage";
		
	}
	
	@PostMapping("/bookingConfirmationPage")
	public String confirmationBooking(HttpSession session, Model model) {
		
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
		model.addAttribute("current_user", session.getAttribute("current_user"));
		User user = (User) session.getAttribute("current_user");
		
		String rType = (String) session.getAttribute("current_roomType");
		model.addAttribute("current_roomType", rType);
		RoomType roomType = RoomType.valueOf(rType);
		
		String checkInDate = (String) session.getAttribute("current_checkIn");
		model.addAttribute("current_checkInDate", checkInDate);
		LocalDate checkIn = dateService.convertFromStringToLocalDate(checkInDate);
		
		String checkOutDate = (String) session.getAttribute("current_checkOut");
		model.addAttribute("current_checkOutDate", checkOutDate);
		LocalDate checkOut = dateService.convertFromStringToLocalDate(checkOutDate);
		
		Room room = (Room) session.getAttribute("room");
		
		Booking booking = new Booking(user,room, checkIn, checkOut);
		user.addBooking(booking);
		bookingService.confirmBooking(booking);
		room.addToRoomReservedDatesList(booking);
		roomService.confirmReservedDates(booking);
		
		session.setAttribute("booking",booking);
		
		return "redirect:/afterBookingPage";
		
	}
	
	@GetMapping("/afterBookingPage")
	public String goToAfterBookingPage(HttpSession session,  Model model) {
		
//		String username = (String) session.getAttribute("current_user");
//		User user = userService.findUserByUsername(username);
//		model.addAttribute("current_user", user);
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		User user = (User) session.getAttribute("current_user");
		
		String rType = (String) session.getAttribute("current_roomType");
		model.addAttribute("current_roomType", rType);
		RoomType roomType = RoomType.valueOf(rType);
		
		String checkInDate = (String) session.getAttribute("current_checkIn");
		model.addAttribute("current_checkInDate", checkInDate);
		LocalDate checkIn = dateService.convertFromStringToLocalDate(checkInDate);
		
		String checkOutDate = (String) session.getAttribute("current_checkOut");
		model.addAttribute("current_checkOutDate", checkOutDate);
		LocalDate checkOut = dateService.convertFromStringToLocalDate(checkOutDate);
		
		return "/afterBookingPage";
	}
	
	
	
	// apparently no need after spring security
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		return "redirect:/";
	}
}
