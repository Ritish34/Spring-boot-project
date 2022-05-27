package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.example.ems.dao.AddressDaoInterface;
import com.example.ems.dao.UserDaoInterface;
import com.example.ems.model.Address;
import com.example.ems.model.User;
import com.example.ems.service.UserServiceImp;
import com.example.ems.util.GeneratePassword;
import com.example.ems.util.SendEmail;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class ServiceTest {
	
	@InjectMocks
	private UserServiceImp service;
	
	@Mock
	private UserDaoInterface userdao;
	
	@Mock
	private AddressDaoInterface addressdao;
	
	@Mock
	private SendEmail mail;
	
	@Mock
	private GeneratePassword gp;
	
	@BeforeAll
	void init() {
		service = new UserServiceImp();
	}

	@Test
	void testLoginUserRightEmailandPass() {
		String email = "ritishparmar34@gmail.com";
		String pass = "Ritish@123";
		
		User expected = new User();
		expected.setId(1);
		expected.setFirst_name("ritish");
		expected.setEmail(email);
		expected.setPassword(pass);
		
		when(userdao.existsByEmail(email)).thenReturn(true);
		when(userdao.findByEmail(email)).thenReturn(expected);
		
		User actual = service.loginUser(email, pass);
		assertNotNull(actual);
		assertEquals(pass, actual.getPassword());
		
		verify(userdao, atLeast(1)).findByEmail(email);
	}
	
	@Test
	void testLoginUserRightEmailandWrongePass() {
		String email = "ritishparmar34@gmail.com";
		String pass = "Ritish@123";
		
		User expected = new User();
		expected.setId(1);
		expected.setFirst_name("ritish");
		expected.setEmail(email);
		expected.setPassword("Ritish@123456");
		
		when(userdao.existsByEmail(email)).thenReturn(true);
		when(userdao.findByEmail(email)).thenReturn(expected);
		
		User actual = service.loginUser(email, pass);
		assertNull(actual);
	}
	
	@Test
	void testLoginUserWrongeEmail() {
		String email = "ritish34@gmail.com";
		String pass = "Ritish@123";
		
		when(userdao.existsByEmail(email)).thenReturn(false);
		
		User actual = service.loginUser(email, pass);
		assertNull(actual);
	}

	@Test
	void testSaveUserWithoutError() {
		ArrayList<Address> Expectedlist = new ArrayList<Address>();
		
		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
				"some xml".getBytes());
		
		User expected = new User();
		expected.setImage1(firstFile);
		expected.setId(15);
		
		when(userdao.save(expected)).thenReturn(expected);
		
		String str = service.saveUser(Expectedlist, expected);
		
		assertEquals("success", str);
	}
	
	@Test
	void testSaveUserWithFileUserNotSave() throws IOException {
		ArrayList<Address> Expectedlist = new ArrayList<Address>();
		
		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
				"some xml".getBytes());
		
		User expected = new User();
		expected.setImage1(firstFile);
		expected.setId(0);
		
		when(userdao.save(expected)).thenReturn(expected);
		
		String str = service.saveUser(Expectedlist, expected);
		
		assertEquals("error", str);
	}
	
	@Test
	void testSaveUserWithoutFile() throws IOException {
		ArrayList<Address> Expectedlist = new ArrayList<Address>();
		
		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
				"".getBytes());
		
		User expected = new User();
		expected.setImage1(firstFile);
		expected.setId(1);
		
		when(userdao.save(expected)).thenReturn(expected);
		
		String str = service.saveUser(Expectedlist, expected);
		
		assertEquals("success", str);
	}
	
	@Test
	void testCheckEmailIsDuplicate() {
		String email = "ritishparmar34@gmail.com";
				
		when(userdao.existsByEmail(email)).thenReturn(true);
		
		String actual = service.checkEmail(email);
		assertEquals("Duplicate", actual);
	}
	
	@Test
	void testCheckEmailIsNotDuplicate() {
		String email = "ritishparmar34@gmail.com";
		
		when(userdao.existsByEmail(email)).thenReturn(false);
		
		String actual = service.checkEmail(email);
		assertEquals("Not Duplicate", actual);
	}

	@Test
	void testGetAllUser() {

		List<User> expectedlist = new ArrayList<User>();
		
		User user = new User();
		user.setRole("user");
		
		expectedlist.add(user);
		
		when(userdao.findAll()).thenReturn(expectedlist);
		
		List<User> list = service.getAllUser();
		assertEquals(expectedlist.toString(), list.toString());
	}

	@Test
	void testDeleteUser() {
		
		int id = 10;
		
		doNothing().when(userdao).deleteById(id);
		
		boolean flag = service.deleteUser(id);
		assertTrue(flag);
	}

	@Test
	void testGetUser() {
		int id = 10;
		
		User expected = new User();
		expected.setId(10);
		expected.setFirst_name("ritish");
		
		when(userdao.findById(id)).thenReturn(Optional.of(expected));
		
		User actual = service.getUser(id);
		
		assertEquals(id, actual.getId());
	}

	@Test
	void testUpdateUser() {
		List<Integer> list1 = new ArrayList<Integer>();
		ArrayList<Address> list = new ArrayList<Address>();
		List<Integer> add = new ArrayList<Integer>();
		
		list1.add(1);
		
		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
				"some xml".getBytes());
		
		User expected = new User();
		expected.setImage1(firstFile);
		
		when(userdao.save(expected)).thenReturn(expected);
		doNothing().when(addressdao).deleteAllById(any());
		
		boolean flag = service.updateUser(expected,list1,list,add);
		assertTrue(flag);
	}
		
	@Test
	void testGetAddresslist() {
		List<Address> addlist = new ArrayList<Address>();
		Address a = new Address();
		a.setAddressid(1);
		addlist.add(a);
		
		List<Integer> list = service.getAddresslist(addlist);
		
		assertNotNull(list);
	}

	@Test
	void testChangePasswordEmailNotFound() {
		String Email = "ritishparmar34@gmail.com";
		
		when(userdao.findByEmail(Email)).thenReturn(null);
		
		String str = service.changePassword(Email);
		
		assertEquals("Email Address is Not Present into Database", str);
	}
	
	@Test
	void testChangePasswordEmailFoundMailSend() {
		String Email = "ritishparmar34@gmail.com";
		String res = "Email Sent Successfully";
		
		User u = new User();
		u.setEmail(Email);
		u.setPassword("temp@123");
		u.setId(50);
		
		when(userdao.findByEmail(Email)).thenReturn(u);
		when(gp.generateRandomPassword(4, 97, 122)).thenReturn("ritish");
		when(gp.generateRandomPassword(3, 48, 57)).thenReturn("123");
		when(userdao.save(any())).thenReturn(u);
		when(mail.sendmail("ritish@123", Email)).thenReturn(res);
		
		String str = service.changePassword(Email);
		
		assertEquals(res, str);
	}
	
	@Test
	void testChangePasswordEmailFoundMailnotsend() {
		String Email = "ritishparmar34@gmail.com";
		String res = "Fail";
		
		User u = new User();
		u.setEmail(Email);
		u.setPassword("temp@123");
		u.setId(50);
		
		when(userdao.findByEmail(Email)).thenReturn(u);
		when(gp.generateRandomPassword(4, 97, 122)).thenReturn("ritish");
		when(gp.generateRandomPassword(3, 48, 57)).thenReturn("123");
		when(userdao.save(any())).thenReturn(u);
		when(mail.sendmail("ritish@123", Email)).thenReturn(res);
		
		String str = service.changePassword(Email);
		
		assertEquals(res, str);
	}
	
	@Test
	void testChangePasswordEmailFoundMailPasswordNotSave() {
		String Email = "ritishparmar34@gmail.com";
		String res = "Fail";
		
		User u = new User();
		u.setEmail(Email);
		u.setPassword("temp@123");
		u.setId(50);
		
		User u1 = new User();
		u.setId(20);
		
		when(userdao.findByEmail(Email)).thenReturn(u);
		when(gp.generateRandomPassword(4, 97, 122)).thenReturn("ritish");
		when(gp.generateRandomPassword(3, 48, 57)).thenReturn("123");
		when(userdao.save(any())).thenReturn(u1);
		
		String str = service.changePassword(Email);
		
		assertEquals(res, str);
	}
	
	@Test
	void updateUserWithExceptionTest() {
		List<Integer> list1 = new ArrayList<Integer>();
		ArrayList<Address> list = new ArrayList<Address>();
		List<Integer> add = new ArrayList<Integer>();
		
		User expected = new User();
		expected.setImage1(null);
		
		boolean flag = service.updateUser(expected,list1,list,add);
		assertFalse(flag);
	}
	
	@Test
	void saveUserWithExceptionTest() {
		ArrayList<Address> list = new ArrayList<Address>();
		
		User expected = new User();
		expected.setImage1(null);
		
		String str = service.saveUser(list,expected);
		
		assertEquals("Error", str);
	}
}
