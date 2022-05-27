package com.example.ems.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;

import com.example.ems.model.User;
import com.example.ems.service.UserService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
	
	@InjectMocks
	private LoginController loginController;
	
	@Mock
	private UserService userservice;
	
	@Mock
	private Model model;
	
	@Mock
	private HttpSession session;
	
	@Test
	void mappingTest() {
		String str = loginController.mapping();
		assertEquals(str, "Error");
	}
	
	@Test
	void getHomeTest() {
		String str = loginController.gethomePage();
		assertEquals(str, "index");
	}
	
	@Test
	void postHomeTest() {
		String str = loginController.posthomePage();
		assertEquals(str, "index");
	}
	
	@Test
	void getForgetPasswordPageTest() {
		String str = loginController.getForgetPasswordPage(model);
		assertEquals(str, "ForgetPassword");
	}

	@Test
	void postForgetPasswordPageTest() {
		String str = loginController.postForgetPasswordPage(model);
		assertEquals(str, "ForgetPassword");
	}
	
	@Test
	void generateNewPasswordSuccess() {
		String email = "ritish@gmail.com";
		
		when(userservice.changePassword(email)).thenReturn("Email Sent Successfully");
		
		String str = loginController.generateNewPassword(email,model);
		
		assertEquals(str, "forward:/home");
	}
	
	@Test
	void generateNewPasswordFail() {
		String email = "ritish@gmail.com";
		
		when(userservice.changePassword(email)).thenReturn("Fails");
		
		String str = loginController.generateNewPassword(email,model);
		
		assertEquals(str, "ForgetPassword");
	}
	
	@Test
	void getLogoutTest() {
		
		String str = loginController.getLogout(session, model);
		
		assertEquals("redirect:/home", str);
	}
	
	@Test
	void getAdminLoginTest() {
		String email = "ritish@gmail.com";
		String pass = "ritish@123";
		
		User expected = new User();
		expected.setEmail(email);
		expected.setPassword(pass);
		expected.setRole("admin");
		
		when(userservice.loginUser(email, pass)).thenReturn(expected);
		
		String str = loginController.getLogin(email, pass, session, model);
		
		assertEquals(str, "redirect:/Admin-Dashboard");
	}
	
	@Test
	void getUserLoginTest() {
		String email = "ritish@gmail.com";
		String pass = "ritish@123";
		
		User expected = new User();
		expected.setEmail(email);
		expected.setPassword(pass);
		expected.setRole("User");
		
		when(userservice.loginUser(email, pass)).thenReturn(expected);
		
		String str = loginController.getLogin(email, pass, session, model);
		
		assertEquals(str, "redirect:/User-Dashboard");
	}
	
	@Test
	void FailLoginTest() {
		String email = "ritish@gmail.com";
		String pass = "ritish@123";
		
		when(userservice.loginUser(email, pass)).thenReturn(null);
		
		String str = loginController.getLogin(email, pass, session, model);
		
		assertEquals(str, "index");
	}
}
