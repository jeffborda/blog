# My Messenger

My Messenger is simple blogging app written using the Spring framework. It has been implemented using a RESTful architure.

 Main features of the app:
- User may create an account by entering a username and password.
- Usernames are checked and must be unique.
- Passwords are stored using hashing and salting methods by bCrypt.
- Users can create blog posts, which are rendered to their profile page.
- Users can discover other users by using the 'Directory' page.
- Users can read other users' posts by viewing their profile pages.
- Users can follow other users by clicking the 'Follow' button on another user's profile.
- When a user is being followed, their blog posts will be listed on the logged in user's 'Feed' page.
- The logged in user can 'Unfollow' other users by clicking the button their profile.

## Routes

| Description         | Route                | http Method |
|---------------------|----------------------|-------------|
|Homepage             |`/`                   |`GET`        |
|New User Registration|`/signup`             |`POST`       |
|Login                |`/login`              |`GET`        |
|User Profile         |`/my-profile`         |`GET`        |
|User Feed            |`my-profile/feed`     |`GET`        |
|Add Blogpost         |`/blogpost`           |`POST`       |
|User Index           |`/users/all`          |`GET`        |
|View Other User      |`/users/{id}`         |`GET`        |
|Add Following User   |`/users/{id}/follow`  |`POST`       |
|Remove Following User|`/users/{id}/unfollow`|`POST`       |


## Dependencies

- Spring Web
- Spring Data JPA
- Spring Boot Dev Tools
- Spring Security
- Thymeleaf
- PostgreSQL Driver


## Upcoming Features

- CSS Styles
- Profile pictures for users
- Ability for users to write a bio
- Ability for users to enter their birthday
- Ability for users to create various blog post pages by topic, instead of posting to profile page

## How to Use

1. Clone repo from github.
2. Open `application.properties`
3. Set environmental variables in `application properties` as required for your machine
4. Run using CLI command: `gradlew bootRun`
5. Open your web browser, go to address: `localhost:8080`

## References

- [Meyer Web CSS Reset](https://meyerweb.com/eric/tools/css/reset/)
- [Hover Dropdown Menu on Feed Page](https://www.w3schools.com/howto/howto_css_dropdown.asp)
