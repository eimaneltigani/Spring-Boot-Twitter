package com.tts.techtalenttwitter.configuration;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//You can define how a class is made with an annotation
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	
	//In here, you write instructions on how to create a BCryptoPasswordEncoder
	//The way we do that is we put a method inside a class with @Configuration
	
	//And the method is annotate with:
	//The method must return the type you are defining how to create
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	

}
