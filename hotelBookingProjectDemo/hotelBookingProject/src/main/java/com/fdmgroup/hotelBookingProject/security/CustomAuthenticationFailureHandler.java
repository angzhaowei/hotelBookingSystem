// keeping this here for future reference of throwing exceptions in spring security

//package com.fdmgroup.hotelBookingProject.security;
//
//import java.io.IOException;
//
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler{
//
//	@Override
//	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException exception) throws IOException, ServletException {
//		
//		response.sendRedirect("redirect:/loginErrorTryAgain");
//		
//	}
//
//}
