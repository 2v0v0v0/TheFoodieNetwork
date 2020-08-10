# The Foodie Network

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
  It is a social networking app that focuses on food and restaurants, where users can connect and explore new tastes across the nation with the people around them. Where the users have the option to write reviews for themself, friends and family, or for everyone.
  
  I came up with this idea because my friends and I love trying new cuisine and we keep a spreadsheet of all the places that we visited. I also realized that I am more likely to visit a restaurant if I saw my friends post about it on social media or they personally recommended it to me. Therefore, I want the app to be a place where people can share new or good food establishments that they have visited to their friends. Alternatively, users can see what trendy or new spots that their friends visited then discover new cuisines through that. I want users to get honest reviews from the people that they can trust.   

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Social network
- **Mobile:** This is a mobile only exclusive product. User can write a review and search for reviews on the go or while visiting a food establishment. 
- **Story:** Allow users to share/recommend restaurants/eateries/food establishments to friends/public. Easy to look up places your friends have visited and recommended.
- **Market:** Anyone who enjoys exploring new restaurants/eateries/food spots and contribute to the foodie network ecosystem.
- **Habit:** Post whenever visit a food spots or when order delivery/takeout. Check-in with friends. Searching for new food to try.
- **Scope:** User profile, posting review (picture), adding/following friends.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can sign up for account 
* Login/out of the app
* Posting review to restaurants + pictures
* Can send/cancel friend requests and accept/delete friend requests
* edit progile and add profile picture
* Two timeline: Home and Global

**Optional Nice-to-have Stories**

* share reviews and recommendations
* User can favorites reviews
* User can bookmark review and view bookmarked reviews

(in process)
* Create events and invite friends
* User can comment on post/review

### 2. Screen Archetypes

* [login page]
   * user can login and out of app
   * go to sigup page
* [signup page]
   * sigup for account
* [Global timeline]
   * New feeds
* [Home timeline]
   * New feeds among friends/ from followings
* [Review compose]
  * write and publish review
* [Search results]
  * search for food establishment to write a review for.
* [People search results]
  * search for people to add as friends.
* [Bookmark]
  * view saved posts.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

Bottom navigation:
* Global feeds
* Home feeds
* Compose

Menu:
* Restaurant search
* People search
* Bookmark
* Profile
* Friend list

Others:
* Log in
* Edit profile
* View other users' profile

**Flow Navigation** (Screen to Screen)
* Log in -> Home -> Bottom navigation and Menu
* Profile -> Edit profile
* Compose <-> Restaurant search

## Wireframes

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 

### Models
#### Review

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the review (default field) |
   | createdAt     | DateTime | date when this review is created (default field) |
   | author        | Pointer<User>| review author |
   | restaurant    | Pointer<Restaurant>| the restaurant the review written for|
   | reviewText    | String   | review text |
   | rating        | Number   | out of 5 rating score, step size: 0.5 |
   | recommend     | Boolean  | Does user want to recommend this place to friends |
   | isGlobal      | Boolean  | set review scope |
   | heart         | Relation<User> | list of users who liked the review |
   | heartCount    | Number   | number of likes of the review |
 
#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | username      | String   | unique name for the user (required) |
   | screenName    | String   | optional screen name for the user |
   | password      | String   | (required) |
   | email         | String   | (optional) |
   | bio           | String   | user bio information(optional) |
   | profileImage  | File     | (optional) |
   | friends       | Relation<User>   | created when user make friend |
   | bookmarked    | Relation<Review>   | List of review that the user bookmarked |
   
#### Restaurant

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the restaurant (default field) |
   | zomatoID      | Number   | unique id from zomato API (required) |
   | name     | String   | Restaurant name |
   | cuisines     | String   |  |
   | address     | String   |  |
   | geoPoint     | GeoPoint   |  |
   
#### Friend Request

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | from     | Pointer<User>   | user who send the request |
   | to    | Pointer<User>   | user the request deliver to |
   | isDeclined     | Boolean   | default value: true, request is declined |
   
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
##### Zomato API
- Base URL - [https://developers.zomato.com/api/v2.1/](https://developers.zomato.com/api/v2.1/)

   HTTP Verb | Endpoint | Description
   ----------|----------|------------
    `GET`    | /search | get list of restaurants
    `GET`    | /locations | Get list of locations
    `GET`    | /geocode | Get location by geocode
    `GET`    | /restaurant | Get restaurant details


