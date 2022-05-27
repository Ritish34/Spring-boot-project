package com.example.ems.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.ems.service.UserService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@InjectMocks
	private UserController userController;
	
    @Mock
    HttpServletRequest request;
	
	@Mock
	private UserService userservice;
	
	@Mock
	private Model model;
	
	@Mock
	private HttpSession session;
	
	@Mock
	private BindingResult br;
		
	@Test
	void getRegistrationPageTest() {
		String str  = userController.getRegistrationPage(model);
		assertEquals("Registration", str);
	}
	
	@Test
	void postRegistrationPageTest() {
		String str  = userController.postRegistrationPage(model);
		assertEquals("Registration", str);
	}
	
	@Test
	void getUserPageTest() {
		String str = userController.getUserPage(model);
		assertEquals("User-Dashboard", str);
	}
	
	@Test
	void checkEmailTest() {
		String email = "ritishparmar34@gmail.com";
		
		when(userController.checkEmail(email)).thenReturn("same/not same");
		
		String str = userController.checkEmail(email);
		assertEquals("same/not same", str);
	}
	
	@Test
	void registerUserTest() {
		when(br.hasErrors()).thenReturn(true);
		List<FieldError> er = new LinkedList<FieldError>();
		FieldError e = new FieldError("br", "first_name", "not empty");
		er.add(e);
		when(br.getFieldErrors()).thenReturn(er);
		String str = userController.registerUser(null, null, br, model);
		assertEquals("forward:/registration-page",str);
	}
	
	@Test
	void registerUserwithRequestTest() throws ServletException{
		when(br.hasErrors()).thenReturn(false);
		
		String[] address= {"abc"};
		String[] zip= {"123456"};
		String[] city = {"Botad"};
		String[] state = {"Gujrat"};
		String[] contry = {"India"};
		
		when(request.getParameterValues("address[]")).thenReturn(address);
		when(request.getParameterValues("zip[]")).thenReturn(zip);
		when(request.getParameterValues("city[]")).thenReturn(city);
		when(request.getParameterValues("state[]")).thenReturn(state);
		when(request.getParameterValues("contry[]")).thenReturn(contry);
		
		when(userservice.saveUser(any(), any())).thenReturn("sucess");
	
		String msg = userController.registerUser(request,null,br,model);
		
		assertEquals("index", msg);
	}
}
