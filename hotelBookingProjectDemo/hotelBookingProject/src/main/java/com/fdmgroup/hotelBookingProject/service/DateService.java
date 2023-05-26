package com.fdmgroup.hotelBookingProject.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.hotelBookingProject.model.ReservedDate;

@Service
public class DateService {

	public boolean checkIfStartDateBeforeEndDate(String startDate, String endDate) {
		
		Date checkInDateFormatted = null;
		Date checkOutDateFormatted = null;
		
		try {
			checkInDateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			checkOutDateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (checkInDateFormatted.compareTo(checkOutDateFormatted)<0) {
			// start before end
			return true;
		} else if (checkInDateFormatted.compareTo(checkOutDateFormatted)>0) {
			// start after end
			return false;
		} else {
			// same day
			return false;
		}
		
	}
	
	public LocalDate convertFromStringToLocalDate(String date) {
		
		LocalDate localDate = LocalDate.parse(date);
		return localDate;
	}
	
	public ArrayList<LocalDate> getAllDatesWithin(LocalDate start, LocalDate end){
		
		ArrayList<LocalDate> dateRange = new ArrayList<>();
        LocalDate date = start;
        while (!date.isAfter(end)) {
            dateRange.add(date);
            date = date.plusDays(1);
        }
        return dateRange;
	}

	public boolean checkForDatesClash(ArrayList<LocalDate> desiredDates, List<ReservedDate> eachRoomDates) {
		// true means date clash, room isnt available on those dates
		// false means no date clash

		// extract out all the LocalDates from the ReservedDates
		ArrayList<LocalDate> reservedDates = new ArrayList<LocalDate>();
		for(ReservedDate rDate: eachRoomDates) {
			reservedDates .add(rDate.getDate());
		}
		
		// time to compare both lists
		return desiredDates.stream().anyMatch(reservedDates::contains);
		
	}

	

}
