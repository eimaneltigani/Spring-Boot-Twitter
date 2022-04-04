package com.tts.techtalenttwitter.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tweet {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="tweet_id")
	private Long id;
	
	//We aren't actually going an entire column of the Tweet database table
	//instead we actually just want to store in our database a user id --
	//which will be a foreign key we can use to look up a user
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	@JoinColumn(name= "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	//Adding certain annotations that tell SB what is and isn't a valid message
	@NotEmpty(message = "Tweet cannot be empty")
	@Length(max = 280, message = "Tweet cannt have more than 280 character")
	private String message;
	
	@CreationTimestamp
	private Date createdAt;

}
