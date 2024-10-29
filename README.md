# Spring Boot Twitter

Twitter MVP in SpringBoot. Contains essential features:
* Users can sign up/login into their account via JBDC authentication
* Users can post/delete tweets and view their feeds
* Users can follow/unfollow eachother and view each others pages
* Users can search tweets by hashtags
* Tweets are formatted with timestamps and shortened links


## Technologies
* Backend: Spring (Boot, Security), SQL (H2 RDBMS)
* Frontend: HTML, CSS, JavaScript
* IDE: Eclipse


## Model-View-Controller Structure

- main/java
  - **_models_**
    - User
    - Role
    - Tweet
    - TweetDisplay
    - Tag
  - **_controllers_**
    - AuthorizationController - handles requests to create new user
    - FollowController - add or remove followers
    - TweetController - create and view tweets
    - UserController - view user profiles
  - service
    - TweetService - logic to format tweets and search by attribute
    - UserService - logic to save and search users
- main/resources/templates - **_(views)_**
