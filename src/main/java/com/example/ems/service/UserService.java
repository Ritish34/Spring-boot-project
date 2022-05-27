package com.example.ems.service;


import com.example.ems.model.Address;
import com.example.ems.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    User loginUser(String email, String pass);

    List<User> getAllUser();
    
    boolean deleteUser(int id);
    
    String saveUser(ArrayList<Address> list,User user);

	User getUser(int theId);

	List<Integer> getAddresslist(List<Address> addlist);

	boolean updateUser(User user, List<Integer> list1, ArrayList<Address> list, List<Integer> add);

	String checkEmail(String email);

	String changePassword(String email);
}
