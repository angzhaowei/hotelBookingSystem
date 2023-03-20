package com.fdmgroup.hotelBookingProject.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	

}
