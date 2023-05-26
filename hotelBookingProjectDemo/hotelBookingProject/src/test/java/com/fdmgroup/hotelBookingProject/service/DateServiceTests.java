package com.fdmgroup.hotelBookingProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fdmgroup.hotelBookingProject.model.ReservedDate;


public class DateServiceTests {

	DateService dateService;
	
	@BeforeEach
	void setUp() {
		dateService = new DateService();
	}
	
	@Test
	@DisplayName("DateService Test1 checkIfStartDateBeforeEndDate() start before end")
	void testStartDateBeforeEndDateMethodWorks_StartBeforeEnd() {
		
		// arrange
		String start1 = "2023-03-18";
		String end1 = "2023-03-19";
		
		String start2 = "2022-03-18";
		String end2 = "2023-05-19";
		
		String start3 = "2019-03-18";
		String end3 = "2025-02-28";
		
		String start4 = "2023-02-28";
		String end4 = "2023-03-01";
		
		boolean expectedOutcome = true;
		boolean actualOutcome1;
		boolean actualOutcome2;
		boolean actualOutcome3;
		boolean actualOutcome4;
		
		
		// act
		actualOutcome1 = dateService.checkIfStartDateBeforeEndDate(start1, end1);
		actualOutcome2 = dateService.checkIfStartDateBeforeEndDate(start2, end2);
		actualOutcome3 = dateService.checkIfStartDateBeforeEndDate(start3, end3);
		actualOutcome4 = dateService.checkIfStartDateBeforeEndDate(start4, end4);

		// assert
		assertEquals(expectedOutcome ,actualOutcome1);
		assertEquals(expectedOutcome ,actualOutcome2);
		assertEquals(expectedOutcome ,actualOutcome3);
		assertEquals(expectedOutcome ,actualOutcome4);
	}
	
	@Test
	@DisplayName("DateService Test2 checkIfStartDateBeforeEndDate() start after end")
	void testStartDateBeforeEndDateMethodWorks_StartAfterEnd() {
		
		// arrange
		String start1 = "2023-03-18";
		String end1 = "2023-03-17";
		
		String start2 = "2022-03-18";
		String end2 = "2021-05-19";
		
		String start3 = "2019-03-18";
		String end3 = "2015-02-28";
		
		String start4 = "2023-03-01";
		String end4 = "2023-02-28";
		
		boolean expectedOutcome = false;
		boolean actualOutcome1;
		boolean actualOutcome2;
		boolean actualOutcome3;
		boolean actualOutcome4;
		
		
		// act
		actualOutcome1 = dateService.checkIfStartDateBeforeEndDate(start1, end1);
		actualOutcome2 = dateService.checkIfStartDateBeforeEndDate(start2, end2);
		actualOutcome3 = dateService.checkIfStartDateBeforeEndDate(start3, end3);
		actualOutcome4 = dateService.checkIfStartDateBeforeEndDate(start4, end4);

		// assert
		assertEquals(expectedOutcome ,actualOutcome1);
		assertEquals(expectedOutcome ,actualOutcome2);
		assertEquals(expectedOutcome ,actualOutcome3);
		assertEquals(expectedOutcome ,actualOutcome4);
	}
	
	@Test
	@DisplayName("DateService Test3 checkIfStartDateBeforeEndDate() start same as end")
	void testStartDateBeforeEndDateMethodWorks_StartSameAsEnd() {
		
		// arrange
		String start1 = "2023-03-18";
		String end1 = "2023-03-18";
		
		String start2 = "2022-03-18";
		String end2 = "2022-03-18";
		
		
		boolean expectedOutcome = false;
		boolean actualOutcome1;
		boolean actualOutcome2;

		
		
		// act
		actualOutcome1 = dateService.checkIfStartDateBeforeEndDate(start1, end1);
		actualOutcome2 = dateService.checkIfStartDateBeforeEndDate(start2, end2);


		// assert
		assertEquals(expectedOutcome ,actualOutcome1);
		assertEquals(expectedOutcome ,actualOutcome2);


	}
	
	@Test
	@DisplayName("DateService Test4 getAllDatesWithin() works same month")
	void testGetAllDatesWithin_SameMonth() {
		
		// arrange
		LocalDate start = LocalDate.of(2023, 03, 18);
		LocalDate middle = LocalDate.of(2023, 03, 19);
		LocalDate end = LocalDate.of(2023, 03, 20);

		ArrayList<LocalDate> expectedOutcome = new ArrayList<>();
		ArrayList<LocalDate> actualOutcome;
		
		expectedOutcome.add(start);
		expectedOutcome.add(middle);
		expectedOutcome.add(end);
		
		// act
		actualOutcome = dateService.getAllDatesWithin(start, end);

		// assert
		assertEquals(expectedOutcome ,actualOutcome);

	}
	
	@Test
	@DisplayName("DateService Test5 getAllDatesWithin() works different month")
	void testGetAllDatesWithin_DiffMonth() {
		
		// arrange
		LocalDate start = LocalDate.of(2023, 02, 28);
		LocalDate middle = LocalDate.of(2023, 03, 01);
		LocalDate end = LocalDate.of(2023, 03, 02);

		ArrayList<LocalDate> expectedOutcome = new ArrayList<>();
		ArrayList<LocalDate> actualOutcome;
		
		expectedOutcome.add(start);
		expectedOutcome.add(middle);
		expectedOutcome.add(end);
		
		// act
		actualOutcome = dateService.getAllDatesWithin(start, end);

		// assert
		assertEquals(expectedOutcome ,actualOutcome);

	}
	
	
	@Test
	@DisplayName("DateService Test6 getAllDatesWithin() works different year")
	void testGetAllDatesWithin_DiffYear() {
		
		// arrange
		LocalDate start = LocalDate.of(2022, 12, 31);
		LocalDate middle = LocalDate.of(2023, 01, 01);
		LocalDate end = LocalDate.of(2023, 01, 02);

		ArrayList<LocalDate> expectedOutcome = new ArrayList<>();
		ArrayList<LocalDate> actualOutcome;
		
		expectedOutcome.add(start);
		expectedOutcome.add(middle);
		expectedOutcome.add(end);
		
		// act
		actualOutcome = dateService.getAllDatesWithin(start, end);

		// assert
		assertEquals(expectedOutcome ,actualOutcome);

	}
	
	// how to test...
//	@Test
//	@DisplayName("DateService Test7 checkForDatesClash works")
//	void testcheckForDatesClash() {
//		
//		// arrange
//		
//		LocalDate start = LocalDate.of(2022, 12, 31);
//		LocalDate end = LocalDate.of(2023, 01, 02);
//		
//		ArrayList<LocalDate> desiredDates = dateService.getAllDatesWithin(start, end);
//		List<ReservedDate> reservedDatesList;
//		// 
//
//		
//		boolean expectedOutcome = true;
//		boolean actualOutcome;
//		
//		// act
//		actualOutcome = dateService.checkForDatesClash(desiredDates, reservedDatesList);
//
//		// assert
//		assertEquals(expectedOutcome ,actualOutcome);
//
//	}
	
	
	
	
	
	
	
	
}
