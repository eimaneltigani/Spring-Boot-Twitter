package com.tts.techtalenttwitter.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tts.techtalenttwitter.model.User;
import com.tts.techtalenttwitter.service.UserService;

//This annotation says we are going to handle web requests in this claass
@Controller
public class AuthorizationController {
		
	@Autowired
	private UserService userService;
		
	//Specify what url (endpoints) do we want to present this page for:
	@GetMapping(value="/login")
	public String login() {
		return "login"; // return value is the name of template well use for a web page
	}
	
	
	//this is going to present a registration form
	@GetMapping(value="/signup")
	public String registration(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "registration";
	}
	
	//when we sign-up with PostMapping that is going to be an actual
	//attempt to sign-up
	@PostMapping(value="/signup")
	public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
		//user at this point holds the input form data that the user has
		//set up for registration
		
		//check to see if the user already exists
		User foundUser = userService.findByUsername(user.getUsername());
		if (foundUser != null) {
			//User already exists.
			//BindingResult is an object we can give errors too
			bindingResult.rejectValue("username", "error.user", "Username is alreay taken");
		}
		
		if(!bindingResult.hasErrors()) {
			//Process a new user
			userService.saveNewUser(user);
			model.addAttribute("success", "Sign up successful!");
			User user2 = new User();
			model.addAttribute("user", user2);
		}
		return "registration";
	}
}
