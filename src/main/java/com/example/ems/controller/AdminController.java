package com.example.ems.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.example.ems.model.Address;
import com.example.ems.model.User;
import com.example.ems.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {
	
	private static Logger log = LogManager.getLogger(AdminController.class);
	
	@Autowired
	private UserService service;
	
	@GetMapping("/Admin-Dashboard")
	public String getAdminPage(Model model) {
		model.addAttribute("website_name", "Employee Management System");
		model.addAttribute("home_page", "Admin-Dashboard");
		model.addAttribute("profile_page", "profile-page");
		return "Admin-Dashboard";
	}
	
	@GetMapping("/profile-page")
	public String getProfile(Model model) {
		model.addAttribute("website_name", "Employee Management System");
		model.addAttribute("home_page", "Admin-Dashboard");
		model.addAttribute("profile_page", "profile-page");
		return "Profile";
	}
	
	@RequestMapping(value = "/ShowAllUser", method = RequestMethod.POST,produces = "application/json")
	@ResponseBody
	public String getUsers(){
		
		log.debug("Ajax Call For All user is Reach To Controller");
		
		ArrayList<User> list =(ArrayList<User>)service.getAllUser();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		JsonObject json = new JsonObject();

		json.add("data", gson.toJsonTree(list));
		
		log.debug("List Of All User Is Sent Back Through Ajax");
		return json.toString();
	}
	
	@PostMapping("/DeleteUser")
	@ResponseBody
	public String deleteUserData(@RequestParam("UserId") int id) {
		boolean flag = service.deleteUser(id);
		log.debug("User Id : "+id+"is Deleted");
		if(flag)
			return "sucess";
		else
			return "Fail";
	}
	
	@GetMapping("/adduser")
	public String getaddNewUser(Model model) {
		model.addAttribute("title", "Add New User");
		model.addAttribute("header", "Add New User Form");
		model.addAttribute("action", "add");
		model.addAttribute("buttun", "Register");
		model.addAttribute("status", "add");
		model.addAttribute("website_name", "Employee Management System");
		model.addAttribute("home_page", "Admin-Dashboard");
		model.addAttribute("profile_page", "profile-page");

		return "Registration";
	}
	
	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public String postaddNewUser(Model model) {
		model.addAttribute("title", "Add New User");
		model.addAttribute("header", "Add New User Form");
		model.addAttribute("action", "add");
		model.addAttribute("buttun", "Register");
		model.addAttribute("status", "add");
		model.addAttribute("website_name", "Employee Management System");
		model.addAttribute("home_page", "Admin-Dashboard");
		model.addAttribute("profile_page", "profile-page");

		return "Registration";
	}
	
	@RequestMapping(path = "/add", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public String registerUser(HttpServletRequest request,@Valid @ModelAttribute("form") User user,
			Model model,BindingResult br) 
	{	
		if(br.hasErrors())  
        {  
			List<FieldError> errorList = br.getFieldErrors();
			List<String> errors = new ArrayList<String>();
			
			for( FieldError er : errorList) {
				errors.add(er.getDefaultMessage());
			}
			
			model.addAttribute("error",errors);
            return "forward:/adduser";  
        }  
		else
		{	
			String[] address = request.getParameterValues("address[]");
			String[] zip = request.getParameterValues("zip[]");
			String[] city = request.getParameterValues("city[]");
			String[] state = request.getParameterValues("state[]");
			String[] contry = request.getParameterValues("contry[]");
			
			ArrayList<Address> list = new ArrayList<Address>();
			//create address object
			for(int i=0;i<zip.length;i++) {
				if(!(address[i].equals("") || city[i].equals("") || contry[i].equals("") 
						|| zip[i].equals(""))) {
					Address obj = new Address();
					obj.setAddress(address[i]);
					obj.setCity(city[i]);
					obj.setContry(contry[i]);
					obj.setState(state[i]);
					obj.setZip(Integer.parseInt(zip[i]));
					obj.setUser(user);
					list.add(obj);
				}	
			}
			String msg = service.saveUser(list,user);
			log.debug("User Save Successfully");
			model.addAttribute("msg", msg);
			return "redirect:/Admin-Dashboard";
		}
	}
	
	@GetMapping("/update-{id}")
	public String getupdateUserPageOpen(@PathVariable int id,Model model,HttpSession s) {
		model.addAttribute("website_name", "Employee Management System");
		if(s.getAttribute("role").equals("admin")) {
			model.addAttribute("home_page", "Admin-Dashboard");
		}
		else {
			model.addAttribute("home_page", "User-Dashboard");
		}
		model.addAttribute("profile_page", "profile-page");
		model.addAttribute("title", "Update User");
		model.addAttribute("header", "Update User Form");
		model.addAttribute("action", "updateUser");
		model.addAttribute("buttun", "Update");
		model.addAttribute("status", "update");
		model.addAttribute("UserId", id);	
		return "Registration";
	}
	
	@RequestMapping(value = "/update-{id}", method = RequestMethod.POST)
	public String updateUserPageOpen(@PathVariable int id,Model model,HttpSession s) {
		model.addAttribute("website_name", "Employee Management System");
		if(s.getAttribute("role").equals("admin")) {
			model.addAttribute("home_page", "Admin-Dashboard");
		}
		else {
			model.addAttribute("home_page", "User-Dashboard");
		}
		model.addAttribute("profile_page", "profile-page");
		model.addAttribute("title", "Update User");
		model.addAttribute("header", "Update User Form");
		model.addAttribute("action", "updateUser");
		model.addAttribute("buttun", "Update");
		model.addAttribute("status", "update");
		model.addAttribute("UserId", id);	
		return "Registration";
	}
	
	@RequestMapping(value = "/GetOneUserData", method = RequestMethod.POST,produces = "application/json")
	@ResponseBody
	public String getUserData(@RequestParam("UserId") int id,HttpSession s) {
		User theUser = service.getUser(id);
		
		if(theUser == null) {
			String str = "User Does Not Exist For Given Id";
			JsonObject json = new JsonObject();
			json.addProperty("data", str);
			return json.toString() ;
		}
		
		theUser.setBase64Image(theUser.getBase64Image());
		
		s.setAttribute("image", theUser.getImage());
		
		ArrayList<User> list = new ArrayList<User>();
		List<Address> list1 = theUser.getList();
		theUser.setList(new ArrayList<Address>());
		list.add(theUser);
		
		s.setAttribute("addlist", service.getAddresslist(list1));
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject json = new JsonObject();
		json.add("data", gson.toJsonTree(list));
		json.add("Address", gson.toJsonTree(list1));
		
		log.debug("Json data for one user (To Display Profile) is send");
		return json.toString();
	}
	
	@PostMapping("/updateUser")
	public String updateUser(HttpServletRequest request,@Valid @ModelAttribute("form") User user,
			Model model,HttpSession s,BindingResult br) {
		
		if(br.hasErrors())  
        {  
			List<FieldError> errorList = br.getFieldErrors();
			List<String> errors = new ArrayList<String>();
			
			for( FieldError er : errorList) {
				errors.add(er.getDefaultMessage());
			}
			
			model.addAttribute("error",errors);
			int id = Integer.parseInt(request.getParameter("userid"));
            return "forward:/update-"+id;  
        }
		else
		{
			int id = Integer.parseInt(request.getParameter("userid"));
			
			String[] addressid = request.getParameterValues("addressid");
			String[] address = request.getParameterValues("address[]");
			String[] zip = request.getParameterValues("zip[]");
			String[] city = request.getParameterValues("city[]");
			String[] state = request.getParameterValues("state[]");
			String[] contry = request.getParameterValues("contry[]");
			
			if (user.getImage1().isEmpty()) {
				user.setImage((byte[]) s.getAttribute("image"));
			}
			user.setId(id);
			
			@SuppressWarnings("unchecked")
			List<Integer> list1 = (List<Integer>) s.getAttribute("addlist");
			
			ArrayList<Address> list = new ArrayList<Address>();
			List<Integer> add = new ArrayList<Integer>();
			//create address object
			for(int i=0;i<zip.length;i++) {
				if(!(address[i].equals("") || city[i].equals("") || contry[i].equals("") 
						|| zip[i].equals(""))) {
					Address obj = new Address();
					if(!addressid[i].equals("")) {
						int ID =Integer.parseInt(addressid[i]);
						add.add(ID);
						obj.setAddressid(ID);
					}
					obj.setAddress(address[i]);
					obj.setCity(city[i]);
					obj.setContry(contry[i]);
					obj.setState(state[i]);
					obj.setZip(Integer.parseInt(zip[i]));
					obj.setUser(user);
					list.add(obj);
				}	
			}
			service.updateUser(user,list1,list,add);
			
			return "redirect:/update-"+id;
		}
	}
}