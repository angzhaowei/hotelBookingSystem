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
		logger.info("Navigated to register user page");
		logger.info("\n");
		
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(@RequestParam String username, @RequestParam("password") String pw) {
		
		if(userService.registerUser(username,pw)) {
			
			logger.info("User "+ username +" has been registered!");
			logger.info("\n");
			
			return "redirect:/login";
		}
		else {
			
			logger.info("Registration failed.");
			logger.info("\n");
			
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
			
			logger.info("User "+ user.getUsername() +" has been registered!");
			logger.info("\n");
			
			return "redirect:/login";
		}
		else {
			
			logger.info("Registration failed.");
			logger.info("\n");
			
			return "registerUserAlreadyExists";
		}

	}
	
	@GetMapping("/login")
	public String goToLogin() {
		
		logger.info("Navigated to login page");
		logger.info("\n");
		
		return "login";
	}
	
	
	
	@GetMapping("/userHomePage")
	public String goToUserHomePage(Principal principal, HttpSession session, Model model) {
		
		//String username = (String) session.getAttribute("current_user");
		String username = principal.getName();
		
		logger.info("User "+ username +" has logged in!");
		logger.info("\n");
		
		//session.setAttribute("current_user", username);
		User user = userService.findUserByUsername(username);
		model.addAttribute("current_user", user);
		
		return "userHomePage";
	}
	
	
	@GetMapping("/userExistingBookings")
	public String goToExistingBookingsPage(HttpSession session, Model model) {
		
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		User user = (User) session.getAttribute("current_user");
		
		logger.info("User "+ user.getUsername() +" navigated to existing bookings!");
		logger.info("\n");
		
		return "userExistingBookings";
	}
	
	@GetMapping("/userNewBooking")
	public String goToNewBookingPage(HttpSession session, Model model) {
		
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		User user = (User) session.getAttribute("current_user");
		
		logger.info("User "+ user.getUsername() +" navigated to make new bookings page!");
		logger.info("\n");
		
		model.addAttribute("roomTypesList", Room.getRoomTypesAsList());
		  
		
		return "userNewBooking";
	}
	
	@PostMapping("/userNewBooking")
	public String tryToMakeBooking(@RequestParam("roomType") String roomType,
			@RequestParam("checkInDate") String checkInDate,
			@RequestParam("checkOutDate") String checkOutDate,
			HttpSession session, Model model) {
		
		//making sure the data from input is correct
		logger.info("Chosen Room Type is: "+ roomType);
		logger.info("Chosen Check in date is "+checkInDate);
		logger.info("Chosen Check out date is "+checkOutDate);
		
		// set model to session
		model.addAttribute("current_user", session.getAttribute("current_user"));
		User user = (User) session.getAttribute("current_user");
		
		// date validity check
		if(checkInDate.isBlank() || checkOutDate.isBlank()) {
			logger.info("Either one of the dates are empty!");
			return"redirect:/userNewBookingAgainEmptyDates";
		}
		else if (!dateService.checkIfStartDateBeforeEndDate(checkInDate, checkOutDate)) {
			logger.info("Start date is same or after end date!");
			return "redirect:/userNewBookingAgainWrongDates";
		}
		
		// needed cuz the roomType is a string
		RoomType rType = RoomType.valueOf(roomType);
		
		// room availability check
		
		if(roomService.atLeastOneRoomWithDatesAvailable(rType, checkInDate, checkOutDate)) {
			
			logger.info("There is at least one room of "+rType+" available for those dates!");
			
			Room room = roomService.getRoomWithAvailableDates(rType, checkInDate, checkOutDate);
			LocalDate checkIn = dateService.convertFromStringToLocalDate(checkInDate);
			LocalDate checkOut = dateService.convertFromStringToLocalDate(checkOutDate);
			Booking booking = new Booking(user, room, checkIn,checkOut);
			
			session.setAttribute("current_roomType", roomType);
			session.setAttribute("current_checkIn", checkInDate);
			session.setAttribute("current_checkOut", checkOutDate);
			session.setAttribute("room", room);
			
			return "redirect:/bookingConfirmationPage";
		} 
		else {
			
			logger.info("There are no rooms of "+rType+" available for those dates!");
			
			return "redirect:/userNewBookingAgainDatesUnavailable";
		}
		

	}
	
	
	@GetMapping("/userNewBookingAgainEmptyDates")
	public String goToChooseBookingAgainEmptyDates(HttpSession session, Model model) {
		
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		User user = (User) session.getAttribute("current_user");
		
		logger.info("User "+ user.getUsername() +" sent to new booking again, empty date(s) page!");
		logger.info("\n");
		
		model.addAttribute("roomTypesList", Room.getRoomTypesAsList());
		
		return"/userNewBookingAgainEmptyDates";
	}
	

	
	@GetMapping("/userNewBookingAgainWrongDates")
	public String goToChooseBookingAgainWrongDates(HttpSession session, Model model) {
		
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		User user = (User) session.getAttribute("current_user");
		
		logger.info("User "+ user.getUsername() +" sent to new booking again, wrong dates page!");
		logger.info("\n");
		
		model.addAttribute("roomTypesList", Room.getRoomTypesAsList());
		
		return"/userNewBookingAgainWrongDates";
	}
	

	
	@GetMapping("/userNewBookingAgainDatesUnavailable")
	public String goToChooseBookingAgainDatesUnavailable(HttpSession session, Model model) {
		
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		User user = (User) session.getAttribute("current_user");
		
		logger.info("User "+ user.getUsername() +" sent to new booking again, unavailable dates page!");
		logger.info("\n");
		
		model.addAttribute("roomTypesList", Room.getRoomTypesAsList());
		
		return"/userNewBookingAgainDatesUnavailable";
	}
	

	
	@GetMapping("/bookingConfirmationPage")
	public String goToConfirmationPage(HttpSession session, Model model) {
		
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		User user = (User) session.getAttribute("current_user");
		
		logger.info("User "+ user.getUsername() +" sent to booking confirmation page!");
		
		
		String rType = (String) session.getAttribute("current_roomType");
		model.addAttribute("current_roomType", rType);
		
		String checkInDate = (String) session.getAttribute("current_checkIn");
		model.addAttribute("current_checkInDate", checkInDate);
		
		String checkOutDate = (String) session.getAttribute("current_checkOut");
		model.addAttribute("current_checkOutDate", checkOutDate);
		
		logger.info("The booking is tentatively for " + rType + " starting "+ checkInDate + " and ending on "+checkOutDate);
		logger.info("\n");
		
		return "/bookingConfirmationPage";
		
	}
	
	@PostMapping("/bookingConfirmationPage")
	public String confirmationBooking(HttpSession session, Model model) {
		
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
		
		logger.info("The booking is confirmed for " + rType + " starting "+ checkInDate + " and ending on "+checkOutDate);
		logger.info("\n");
		
		return "redirect:/afterBookingPage";
		
	}
	
	@GetMapping("/afterBookingPage")
	public String goToAfterBookingPage(HttpSession session,  Model model) {
		
		model.addAttribute("current_user", session.getAttribute("current_user"));
		
		User user = (User) session.getAttribute("current_user");
		
		logger.info("User "+ user.getUsername() +" sent to after booking page!");
		logger.info("\n");
		
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
	
	
	
	// apparently no need after spring security...? Just keep for future reference on how to invalidate session.
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		return "redirect:/";
	}
}
