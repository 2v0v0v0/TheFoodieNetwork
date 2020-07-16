# The Foodie Network

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Social club for foodies of all experience levels. To connect and explore new taste across the nation. You write reviews for yourself and your friends. 

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Social
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
* User can search for restaurants
* Posting review to restaurants + pictures
* Have followers and following friends
* add profile picture
* Feed/timeline

**Optional Nice-to-have Stories**

* Create events and invite friends
* share reviews and recommendations
* User can favorites reviews
* User can bookmark review and view bookmarked reviews
* User can comment on post/review
*set limit rate for review user can write for restaurant in a day.
- user cannot write review for the same restaurant more than once within 24 hours.
- user cannot write more than 10 reviews in a day.

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

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Global feeds
* Home feeds
* Compose

Optional:
* Bookmark
* Profile

**Flow Navigation** (Screen to Screen)
* 

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
#### Review

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | author        | Pointer to User| image author |
   | image         | File     | image that user posts |
   | text          | String   | review text |
   | rating        | Number   | out of 5 rating score |
   | recommend     | Boolean  | Does user want to recommend this place to friends |
   | commentsCount | Number   | number of comments that has been posted to a review |
   | likesCount    | Number   | number of likes for the post |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
 
#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | username      | String   | unique name for the user (required) |
   
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
##### Zomato API
- Base URL - [https://developers.zomato.com/api/v2.1/](https://developers.zomato.com/api/v2.1/)

   HTTP Verb | Endpoint | Description
   ----------|----------|------------
    `GET`    | /search | Search for restaurants


