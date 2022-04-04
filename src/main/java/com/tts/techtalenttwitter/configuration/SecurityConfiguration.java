package com.tts.techtalenttwitter.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//This is going to be used to setup Spring Security
//First, we have to inherit from WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private DataSource dataSource;
	
	//from configuration file
	@Value("${spring.queries.users-query}")
	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;
	
	
	//This configured the authentication process.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.usersByUsernameQuery(usersQuery)
			.authoritiesByUsernameQuery(rolesQuery)
			.dataSource(dataSource)
			.passwordEncoder(bCryptPasswordEncoder);
	}
	
	//This method is used to configure what is accessible when you're logged in
	//and what's accessible when you are logged in 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//basically says we're gonna authorize requests and we're gonna do it
		//by looking at the URL
		http.authorizeRequests()
			.antMatchers("/console/**").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/signup").permitAll()
			.antMatchers("/custom.js").permitAll()
			.antMatchers("/custom.css").permitAll()
			.antMatchers().hasAuthority("USER").anyRequest()
			.authenticated()
		.and()
		.formLogin()
			.loginPage("/login").failureUrl("/login?error=true")
			.defaultSuccessUrl("/tweets")
			.usernameParameter("username")
			.passwordParameter("password")
		.and()
		.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login")
		.and()
		.exceptionHandling();
		
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
	
	//specifies what can be accessed from the file side
	//configure from both sides to make it clear what is legal to access and whats not
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
			.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

}
