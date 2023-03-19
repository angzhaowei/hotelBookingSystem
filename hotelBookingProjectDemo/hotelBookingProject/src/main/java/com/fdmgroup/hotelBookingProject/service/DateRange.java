package com.fdmgroup.hotelBookingProject.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DateRange implements Iterable<LocalDate> {

	private LocalDate startDate;
	private LocalDate endDate;
	
	
	@Override
	public Iterator<LocalDate> iterator() {
		return stream().iterator();
	}
	
	public Stream<LocalDate> stream() {
	    return Stream.iterate(startDate, d -> d.plusDays(1))
	                 .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1);
	}
	
	public ArrayList<LocalDate> toList() { //could also be built from the stream() method
	    ArrayList<LocalDate> dates = new ArrayList<> ();
	    for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
	      dates.add(d);
	    }
	    return dates;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	

}
