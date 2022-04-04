package com.tts.techtalenttwitter.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tts.techtalenttwitter.model.Role;
import com.tts.techtalenttwitter.model.User;
import com.tts.techtalenttwitter.repository.RoleRepository;
import com.tts.techtalenttwitter.repository.UserRepository;

//This means that we can later ask Springboot to create a
//UserService, and since it is marked with @Service, SB will know
//that it can just go ahead and create one with
//using the UserService constructor to make one
@Service
public class UserService {
	//this is called injection..it is asking SB to automatically
	//create the class and fill it in

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//Something is going to have to call this constructor at some point
	//to create a user service.. We are never going to call it
	//instead we want SB to call this constructor when we ask
	//for a UserService
	@Autowired
	public UserService(UserRepository userRepository,
					   RoleRepository roleRepository,
					   BCryptPasswordEncoder bCryptPasswordEncoder)
	{
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public List<User> findAll() {
		return (List<User>)userRepository.findAll();
	}
	
	public void save(User user) {
		userRepository.save(user);
	}
	
	public User saveNewUser(User user) {
		//Encrypt the password
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRepository.findByRole("USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		
		return userRepository.save(user); // returned user is the final copy
										//saved to database and id will be set
	}
	
	public User getLoggedInUser() {
		String loggedInUsername = 
				SecurityContextHolder.getContext().getAuthentication().getName();
		
		return findByUsername(loggedInUsername);
		
	}
	
}
