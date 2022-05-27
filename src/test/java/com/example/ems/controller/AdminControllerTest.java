package com.example.ems.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.ems.model.Address;
import com.example.ems.model.User;
import com.example.ems.service.UserService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

	@InjectMocks
	private AdminController adminController;
	
	@Mock
	private BindingResult br;
	
	@Spy
    private MockHttpServletRequest request;
	
	@Mock
	private UserService userservice;
	
	@Mock
	private Model model;
	
	@Spy
	private MockHttpSession session;
	
	@Test
	void getAdminPageTest() throws Exception {
		String str = adminController.getAdminPage(model);
		assertEquals("Admin-Dashboard", str);
	}
	
	@Test
	void getProfileTest() {
		String str = adminController.getProfile(model);
		assertEquals("Profile", str);
	}
	
	@Test
	void deleteUserDataSuccessTest() {
		
		int id = 10;
		when(userservice.deleteUser(id)).thenReturn(true);
		String str = adminController.deleteUserData(id);
		assertEquals("sucess", str);
	}
	
	@Test
	void deleteUserDataFailsTest() {
		
		int id = 10;
		when(userservice.deleteUser(id)).thenReturn(false);
		String str = adminController.deleteUserData(id);
		assertEquals("Fail", str);
	}
	
	@Test
	void getaddNewUserTest() {
		String str = adminController.getaddNewUser(model);
		assertEquals("Registration", str);
	}
	
	@Test
	void postaddNewUserTest() {
		String str = adminController.postaddNewUser(model);
		assertEquals("Registration", str);
	}
	
	@Test
	void getupdateadminPageOpenTest() {
		int id = 10;
		
		when(session.getAttribute("role")).thenReturn("admin");
		
		String str = adminController.getupdateUserPageOpen(id,model,session);
		assertEquals("Registration", str);
	}
	
	@Test
	void getupdateUserPageOpenTest() {
		int id = 10;
		
		when(session.getAttribute("role")).thenReturn("User");
		
		String str = adminController.getupdateUserPageOpen(id,model,session);
		assertEquals("Registration", str);
	}
	
	@Test
	void updateAdminPageOpenTest() {
		int id = 10;
		
		when(session.getAttribute("role")).thenReturn("admin");
		
		String str = adminController.updateUserPageOpen(id,model,session);
		assertEquals("Registration", str);
	}
	
	@Test
	void updateUserPageOpenTest() {
		int id = 10;
		
		when(session.getAttribute("role")).thenReturn("User");
		
		String str = adminController.updateUserPageOpen(id,model,session);
		assertEquals("Registration", str);
	}
	
	@Test
	void getUsersTest() {
		
		ArrayList<User> list = new ArrayList<User>();
		
		when(userservice.getAllUser()).thenReturn(list);
		
		String str = adminController.getUsers();
		
		assertEquals("{\"data\":[]}", str);
	}
	
	@Test
	void getUserDataWithNullUserTest() {
		
		int id = 10;
		
		when(userservice.getUser(id)).thenReturn(null);
		
		String str = adminController.getUserData(id, session);
		
		assertEquals("{\"data\":\"User Does Not Exist For Given Id\"}", str);
	}
	
	@Test
	void getUserDataWithUserTest() {
		byte[] image = "one Image".getBytes();
		int id = 10;
		
		String expectedoutput = "{\"data\":[{\"id\":0,\"first_name\":\"Ritish\",\"last_name\":\"Parmar\","
				+ "\"email\":\"ritish@gmail.com\",\"image\":[111,110,101,32,73,109,97,103,101],"
				+ "\"list\":[],\"base64Image\":\"b25lIEltYWdl\"}],"
				+ "\"Address\":[{\"addressid\":1,\"user\":{\"id\":0,\"first_name\":"
				+ "\"Ritish\",\"last_name\":\"Parmar\",\"email\":\"ritish@gmail.com\","
				+ "\"image\":[111,110,101,32,73,109,97,103,101],\"list\":[],\"base64Image\":\"b25lIEltYWdl\"},"
				+ "\"address\":\"abc\",\"zip\":123456,\"city\":\"Botad\","
				+ "\"state\":\"Gujrat\",\"contry\":\"India\"}]}";
		
		User u = new User();
		u.setImage(image);
		u.setFirst_name("Ritish");
		u.setLast_name("Parmar");
		u.setEmail("ritish@gmail.com");
		
		List<Address> list = new ArrayList<Address>();
		Address a = new Address();
		a.setAddress("abc");
		a.setAddressid(1);
		a.setCity("Botad");
		a.setContry("India");
		a.setState("Gujrat");
		a.setUser(u);
		a.setZip(123456);
		list.add(a);
		
		u.setList(list);
		
		when(userservice.getUser(id)).thenReturn(u);
		
		String str = adminController.getUserData(id, session);
		
		assertEquals(str, expectedoutput);
	}
	
//---------------------------------------------------------	
	
	@Test
	void registerUserTest() {
		when(br.hasErrors()).thenReturn(true);
		List<FieldError> er = new LinkedList<FieldError>();
		FieldError e = new FieldError("br", "first_name", "not empty");
		er.add(e);
		when(br.getFieldErrors()).thenReturn(er);
		String str = adminController.registerUser(null, null, model, br);
		assertEquals("forward:/adduser",str);
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
	
		String msg = adminController.registerUser(request,null,model,br);
		
		assertEquals("redirect:/Admin-Dashboard", msg);
	}
	
	@Test
	void updateUserWithErrorTest() {
		String id = "5";
		request.setParameter("userid", id);
		when(br.hasErrors()).thenReturn(true);
		List<FieldError> er = new LinkedList<FieldError>();
		FieldError e = new FieldError("br", "first_name", "not empty");
		er.add(e);
		when(br.getFieldErrors()).thenReturn(er);
		String str = adminController.updateUser(request, null, model,session,br);
		assertEquals("forward:/update-"+id , str);
	}
	
	@Test
	void updateUserWithoutErrorWithImageTest() {
		String id = "5";
		
		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
				"some xml".getBytes());
		
		User u = new User();
		u.setEmail("ritish@gmail.com");
		u.setImage1(firstFile);
		
		List<Integer> testlist = new ArrayList<Integer>();
		
		String[] addressid = {"1"};
		String[] address= {"abc"};
		String[] zip= {"123456"};
		String[] city = {"Botad"};
		String[] state = {"Gujrat"};
		String[] contry = {"India"};
		
		session.setAttribute("addlist", testlist);
		request.setParameter("userid", id);
		request.setParameter("addressid", addressid);
		request.setParameter("address[]", address);
		request.setParameter("zip[]", zip);
		request.setParameter("city[]", city);
		request.setParameter("state[]", state);
		request.setParameter("contry[]", contry);
		
		when(userservice.updateUser(any(), any(), any(), any())).thenReturn(true);
		
		String str = adminController.updateUser(request, u, model,session,br);
		
		assertEquals("redirect:/update-"+id, str);
	}
	
	@Test
	void updateUserWithoutErrorWithoutImageTest() {
		String id = "5";
		
		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
				"".getBytes());
		
		byte[] b = "one image".getBytes();
		
		User u = new User();
		u.setEmail("ritish@gmail.com");
		u.setImage1(firstFile);
		
		List<Integer> testlist = new ArrayList<Integer>();
		
		String[] addressid = {"1"};
		String[] address= {"abc"};
		String[] zip= {"123456"};
		String[] city = {"Botad"};
		String[] state = {"Gujrat"};
		String[] contry = {"India"};
		
		session.setAttribute("image", b);
		session.setAttribute("addlist", testlist);
		request.setParameter("userid", id);
		request.setParameter("addressid", addressid);
		request.setParameter("address[]", address);
		request.setParameter("zip[]", zip);
		request.setParameter("city[]", city);
		request.setParameter("state[]", state);
		request.setParameter("contry[]", contry);
		
		when(userservice.updateUser(any(), any(), any(), any())).thenReturn(true);
		
		String str = adminController.updateUser(request, u, model,session,br);
		
		assertEquals("redirect:/update-"+id, str);
	}
}
