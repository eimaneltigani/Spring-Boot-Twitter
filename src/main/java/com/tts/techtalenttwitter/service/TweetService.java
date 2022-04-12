package com.tts.techtalenttwitter.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tts.techtalenttwitter.model.Tag;
import com.tts.techtalenttwitter.model.Tweet;
import com.tts.techtalenttwitter.model.TweetDisplay;
import com.tts.techtalenttwitter.model.User;
import com.tts.techtalenttwitter.repository.TagRepository;
import com.tts.techtalenttwitter.repository.TweetRepository;

@Service
public class TweetService {
	
	@Autowired
	private TweetRepository tweetRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	public List<TweetDisplay> findAll() {
		List<Tweet> tweets = tweetRepository.findAllByOrderByCreatedAtDesc();
		return formatTweets(tweets);
	}
	
	public List<TweetDisplay> findAllByUser(User user) {
		List<Tweet> tweets = tweetRepository.findAllByUserOrderByCreatedAtDesc(user);
		return formatTweets(tweets);
	}
	
	public List<TweetDisplay> findAllByUsers(List<User> users) {
		List<Tweet> tweets = tweetRepository.findAllByUserInOrderByCreatedAtDesc(users);
		return formatTweets(tweets);
	}
	
	public List<TweetDisplay> findAllWithTag(String tag) {
		List<Tweet> tweets = tweetRepository.findByTags_PhraseOrderByCreatedAtDesc(tag);
		return formatTweets(tweets);
	}
	
	public void save(Tweet tweet) {
		handleTags(tweet);
		tweetRepository.save(tweet);
	}
	
	private void handleTags(Tweet tweet) {
		List<Tag> tags = new ArrayList<Tag>();
		Pattern pattern = Pattern.compile("#\\w+");
		Matcher matcher = pattern.matcher(tweet.getMessage());
		
		while (matcher.find()) {
			String phrase = matcher.group().substring(1).toLowerCase();
			
			Tag tag = tagRepository.findByPhrase(phrase);
			if (tag == null) {
				tag = new Tag();
				tag.setPhrase(phrase);
				tagRepository.save(tag);
			}
			tags.add(tag);
		}
		tweet.setTags(tags);
	}
	
	private List<TweetDisplay> formatTweets(List<Tweet> tweets) {
		addTagLinks(tweets);
		shortenLinks(tweets);
		List<TweetDisplay> displayTweets = formatTimestamps(tweets);
		return displayTweets;
	}

	private List<TweetDisplay> formatTimestamps(List<Tweet> tweets) {
		List<TweetDisplay> response = new ArrayList<>();
		SimpleDateFormat simpleDate = new SimpleDateFormat("M/d/yy");
		PrettyTime prettyTime = new PrettyTime();
		
		Date now = new Date();
		for (Tweet tweet: tweets) {
			TweetDisplay tweetDisplay = new TweetDisplay();
			tweetDisplay.setUser(tweet.getUser());
			tweetDisplay.setMessage(tweet.getMessage());
			tweetDisplay.setTags(tweet.getTags());
			
			long diffInMillis = now.getTime() - tweet.getCreatedAt().getTime();
			//library that gives us number of days
			long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
			if (diff >3) {
				// M/d/yy format
				tweetDisplay.setDate(simpleDate.format(tweet.getCreatedAt())); 
			} else {
				//we want our relative time of how long ago
				tweetDisplay.setDate(prettyTime.format(tweet.getCreatedAt())); 
			}
			response.add(tweetDisplay);
		}
		return response;
	}

	private void shortenLinks(List<Tweet> tweets) {
		Pattern pattern = Pattern.compile("https?[^ ]+");
		for (Tweet tweet : tweets) {
			String message = tweet.getMessage();
			Matcher matcher = pattern.matcher(message);
			
			while (matcher.find()) {
				String link = matcher.group();
				String shortenedLink = link;
				
				if (link.length() > 23) {
					shortenedLink = link.substring(0, 20) + "...";
				}
				message = message.replace(link, 
						"<a class=\"tag\" href=\"" + link + "\" target=\"_blank\">" + shortenedLink + "</a>");
			}
			tweet.setMessage(message);
		}
		
	}

	private void addTagLinks(List<Tweet> tweets) {
		Pattern pattern = Pattern.compile("#\\w+");
		for (Tweet tweet : tweets) {
			String message = tweet.getMessage();
			Matcher matcher = pattern.matcher(message);
			Set<String> tags = new HashSet<String>();
			while (matcher.find()) {
				tags.add(matcher.group());
			}
			for (String tag : tags) {
				String link = "<a class=\"tag\" href=\"/tweets/";
				link += tag.substring(1).toLowerCase();
				link += "\">"+ tag + "</a>";
				message = message.replaceAll(tag, link);
			}
			tweet.setMessage(message);
		}
	}
	
	
}
