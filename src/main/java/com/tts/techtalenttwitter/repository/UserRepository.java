package com.tts.techtalenttwitter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.techtalenttwitter.model.User;

//This is the interface by which we access the
//database table that holds User objects

//We do not actually write the code for the repository
//instead we create an interface that acts as specification
//for what we want, and SPRING BOOT creates the actual
//repository

//To declare that something is a repository in Springboot,
//we can do that by inheriting from Repository --
@Repository
public interface UserRepository extends CrudRepository<User,Long> {
	//The basic methods we add with extends CrudRepository
	//are basically pretty the bare minimum interface we could
	//want to use to talk to a database
	

	User findByUsername(String username);

}
