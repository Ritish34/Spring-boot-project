package com.example.ems.controller;

import com.example.ems.model.User;
import com.example.ems.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
public class LoginController {
	private static Logger log = LogManager.getLogger(LoginController.class);

	@Autowired
	private UserService service;
	
	@GetMapping("/*")
	public String mapping() {
		return "Error"; 
	}
	
    @GetMapping("/home")
    public String gethomePage() {
        return "index";
    }

	@PostMapping("/home")
	public String posthomePage() {
		return "index";
	}
    
    @GetMapping("/forgetpass")
	public String getForgetPasswordPage(Model model){
		
		return "ForgetPassword";
	}
	
	@RequestMapping(value = "/forgetpass", method = RequestMethod.POST)
	public String postForgetPasswordPage(Model model){
		
		return "ForgetPassword";
	}

	@PostMapping("/login")
	public String getLogin(@RequestParam("email") String email, @RequestParam("password") String pass,
						   HttpSession session, Model model) {
		log.debug("Login Mapping Called");

		User u = service.loginUser(email, pass);
		model.addAttribute("object", u);

		if(u != null && u.getRole().equals("admin")) {
			session.setAttribute("username", u.getFirst_name());
			session.setAttribute("id", u.getId());
			session.setAttribute("role", u.getRole());

			log.debug("Admin Login Sucessfull");
			return "redirect:/Admin-Dashboard";
		}
		else if(u != null && u.getRole().equals("User")){
			session.setAttribute("username", u.getFirst_name());
			session.setAttribute("id", u.getId());
			session.setAttribute("role", u.getRole());

			log.debug("User Login Sucessfully");
			return "redirect:/User-Dashboard";
		}
		else {
			String msg = "Username/Password is incorrect";
			model.addAttribute("msg", msg);
			model.addAttribute("email", email);

			log.debug("Login Fails");
			return "index";
		}
	}

	@GetMapping({"/logout","/"})
	public String getLogout(HttpSession session,Model model) {

		session.removeAttribute("username");
		session.removeAttribute("id");
		session.invalidate();

		log.debug("Logout Called");
		log.debug("Session Invalidate");
		return "redirect:/home";
	}
	
	@PostMapping("/sendEmail")
	public String generateNewPassword(@RequestParam("email") String email,Model model) {
		String msg = service.changePassword(email);
		log.debug(msg);
		model.addAttribute("msg", msg);
		
		if(msg == "Email Sent Successfully"){
			return "forward:/home";
        }
        else{
        	return "ForgetPassword";
        }
	}
}
