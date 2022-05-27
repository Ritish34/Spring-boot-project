package com.example.ems.service;

import com.example.ems.dao.AddressDaoInterface;
import com.example.ems.dao.UserDaoInterface;
import com.example.ems.model.Address;
import com.example.ems.model.User;
import com.example.ems.util.GeneratePassword;
import com.example.ems.util.SendEmail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService{
    private static Logger log = LogManager.getLogger(UserServiceImp.class);

    @Autowired
    private UserDaoInterface userDAO;
    
    @Autowired
    private AddressDaoInterface addressDAO;
    
	@Autowired
	private GeneratePassword gp;
	
	@Autowired
	private SendEmail mail;

    public User loginUser(String email, String pass) {

        if(userDAO.existsByEmail(email)){
            User u = userDAO.findByEmail(email);
            if(u != null && pass.equals(u.getPassword()))
                return u;
            else
                return null;
        }
        else
            return null;
    }

    @Override
    public List<User> getAllUser() {
        List<User> list = userDAO.findAll();
        List<User> newlist = new ArrayList<User>();
        for(User user : list) {
        	if(!(user.getRole().equalsIgnoreCase("admin"))) {
        		newlist.add(user);
        	}
        }
        return newlist;
    }

	@Override
	public boolean deleteUser(int id) {
		userDAO.deleteById(id);
		return true;
	}

	@Override
	public String saveUser(ArrayList<Address> list, User user) {
		try {
			if(!(user.getImage1().isEmpty())) {
				user.setImage(user.getImage1().getBytes());
			}
			else {
				InputStream newinputStream = new ClassPathResource("img/defualtimage.jpg").getInputStream();
				user.setImage(newinputStream.readAllBytes());
				newinputStream.close();
			}
			user.setRole("User");
			
			user.setList(list);
			
			User u = userDAO.save(user);
			
			if(u.getId() > 0) {
				return "success";
			}
			else {
				return "error";
			}
		} catch (NullPointerException | IOException e) {
			log.debug(e);
			return "Error";
		}
	}
	
	@Override
	public User getUser(int theId) {
		return userDAO.findById(theId).get();
	}
	
	@Override
	public List<Integer> getAddresslist(List<Address> addlist) {
		List<Integer> list = new ArrayList<Integer>();
		
		for(Address a : addlist) {
			list.add(a.getAddressid());
		}
		
		return list;
	}
	
	@Override
	public boolean updateUser(User user, List<Integer> list1,
			ArrayList<Address> list, List<Integer> add) {
		try {
			if(!(user.getImage1().isEmpty())) {
			
				user.setImage(user.getImage1().getBytes());
			}
			user.setRole("User");
			
			user.setList(list);
			userDAO.save(user);
			
			list1.removeAll(add);
			if(!(list1.isEmpty())) {
				addressDAO.deleteAllById(list1);
			}
			return true;
		} catch (NullPointerException | IOException e) {
			log.debug(e);
			return false;
		}
	}
	
	@Override
	public String checkEmail(String email) {
		boolean flag = userDAO.existsByEmail(email);
		
		if(flag) {
			return "Duplicate";
		}
		else
			return "Not Duplicate";
	}
	
	@Override
	public String changePassword(String email) {

		User u = userDAO.findByEmail(email);
		
		if(u == null) {
			return "Email Address is Not Present into Database";
		}
				
		StringBuilder sb = new StringBuilder();
		sb.append(gp.generateRandomPassword(4, 97, 122));
		sb.append("@");
		sb.append(gp.generateRandomPassword(3, 48, 57));
		
		log.debug(sb.toString());
		String temp = u.getPassword();
		u.setPassword(sb.toString());
		
		User user = userDAO.save(u);
		if(user.getId() == u.getId()) {
			String mailresponse =mail.sendmail(sb.toString(), email);
			if(mailresponse.equals("Fail")) {
				u.setPassword(temp);
				userDAO.save(u);
			}
			return mailresponse;
		}
		else
		{
			return "Fail";
		}
	}
}
