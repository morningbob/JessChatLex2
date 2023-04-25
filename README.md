# Jess Chat Bot

&nbsp;

## Jess Chat Bot Android Mobile App

&nbsp;
&nbsp;

### Jess Chat Bot lets users talk to a chatbot.  The chatbot is powered by Amazon Lex.  A user needs to provide a name, an email and a password to register an account.  The email is used as a username to login to the account.  The chatbot is only available after logging in.

&nbsp;

### The chatbot is configured for general chats.  For example, users can talk to it about the economy, pets etc.  It is only configured to talk in English.  The chats will be recorded and saved in the user’s device.  Users can read the records by clicking the record menu.

&nbsp;
&nbsp;

<img src=".\images\chat_01.png" alt="application register account screenshot" style="width:250px; margin-left: auto; margin-right: auto; display: block;" />

&nbsp;
<center> Registration screen, a user need to register an account and login before he can send message to the chat bot. </center>
&nbsp;

<img src=".\images\chat_02.png" alt="application login screenshot" style="width:250px; margin-left: auto; margin-right: auto; display: block;" />

&nbsp;
<center> Login screen, a user can create account, verify email and reset password in this screen.</center>
&nbsp;

<img src=".\images\chat_03.png" alt="application reset password screenshot" style="width:250px; margin-left: auto; margin-right: auto; display: block;" />

&nbsp;
<center> Reset password screen, a user can reset password here.  After a user requested to reset password, the server will send a confirmation code to the user's registered email.  The user needs to enter the confirmation code in this screen in order to reset the password.</center>
&nbsp;

<img src=".\images\chat_04.png" alt="application home screenshot" style="width:250px; margin-left: auto; margin-right: auto; display: block;" />

&nbsp;
<center> Home screen, which is also the screen to chat with the chat bot.  If there is error getting a response, there will be an error message display on the top of the screen.  There is a bottom navigation bar that the user can use to navigate the app.  The app keeps a record of all the user's chats.  The user can navigate to the record page to read them.  User can also navigate to the profile page.</center>
&nbsp;

<img src=".\images\chat_05.jpg" alt="application profile screenshot" style="width:250px; margin-left: auto; margin-right: auto; display: block;" />

&nbsp;
<center> Profile screen, the user can check his name and email in this page.  The user can also change his password here.</center>
&nbsp;

&nbsp;

## Programming Notes: 

&nbsp;

1. The app is written in Jetpack Compose.  I tried to write all the views as reusable composables.  The app is set up to navigate to different screens, composables, to render relevant info for the users to interact with the app.  Like the Login Screen, the Create Account Screen and Forgot Password Screen.

2. Each screen has its own view model.  The view model hosted nearly all the variables used in the screen.  The view model also handles the business logic.  It modifies the variables and passes them down to the screen.  The screen then changes to reflect the changes.  The screen also gets the user inputs and passes them up to the view model.

3. I also use a main view model to host global info for the app, such as the user’s name and email.  I pass the main view model down to different screens in the navigation setup.

4. I use Amazon Cognito to handle authentication.  Amazon Cognito requires users to verify their email address before logging in.  Users can login the account by using email and password.  Users can also change password, and reset the password.  There is also a user profile page to display user’s info in the account. 

5. I use Amazon Lex to create the chatbot.  I created various intents with utterances as triggers.  Whenever the users say something resembles the utterances, the corresponding intent will be triggered and deliver the response set in the intent.  I use this technique to facilitate the chat.



