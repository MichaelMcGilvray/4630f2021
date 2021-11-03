Question 1 - Project title
Twitch Clips Finder


Question 2 - What is the problem you aim to tackle with your project?
The problem I am to tackle with my project is finding and watching Twitch clips. Twitch is a popular livestreaming 
platform and often viewers don’t have the time or want to watch an entire livestream, so Twitch implemented a 
feature to save and later share short segments of the livestreams. These segments are what Twitch calls clips, and 
they can be between five and sixty seconds long. The issue arises when viewers want to watch clips from a 
particular streamer, category, or day. Currently, there isn’t one place to filter clips by these preferences 
leaving viewers able to only watch a small percentage of the possible clips because the clips were too difficult 
to locate. With my project, I want to help alleviate this issue by providing sufficient filtering capabilities to 
improve the clip finding and watching experience.


Question 3 - Why is this problem important to tackle?
The problem is important to tackle because most Twitch users only watch a small part of a stream, making Twitch 
clips ever more relevant. The current methods of watching clips have glaring oversights and can be fixed and 
improved upon with a few key changes. It will save the users time by allowing them to find what they are looking 
for faster and make the Twitch platform a more enjoyable place.


Question 4 - Who stands to benefit from this app most? (I.e., who is your expected "market")
The people who stand to benefit most from the app are people who use Twitch or are interested in a condensed 
version of the broadcasts on Twitch. If the Twitch clips were easier to find and watch, even people who don’t 
normally watch livestreams may be curious about watching short parts of livestreams.


Question 5 - List other apps that have addressed the same problem (please number them for the next question)
1. Twitch
2. Clips from Twitch
3. Stream Charts (Is just a website and not a mobile app) 

Question 6 - For each app you listed above, list in what ways it is lacking (please number them correspondingly)
App 1
    - The app is lacking a way to filter clips other than by category. 
    - The clips are not the focus of the app and are buried behind other menus, so the users who don’t explore 
      the app may not know that the clips exist.
App 2
    - This app is also lacking in filtering options and only filters by popularity. 
    - When watching clips on this app, it is missing the chatroom replay functionality and most of the screen 
      space is left empty.
    - Once a clip has been selected, the viewer can no longer see the title of the clip, the streamer the clip 
      came from, or the number of views the clip has received.
    - There is no option to go to the timestamp in the livestream when the clip was taken if the viewer would 
      like to watch more than the sixty-second clip.
    - Once the clip has ended, there are no recommended clips given.
App 3
    - The app has much more filtering options than the other two apps but still lacks the ability to filter by 
      a specific date or by multiple streamers.
    - When watching clips, the app is also lacking the chatroom replay functionality.


Question 6 - For each shortcoming you have identified above, state whether you intend to address it, and 
(briefly) how
Since all the apps have similar shortcomings, I’ll skip over the duplicates.
    - To improve the filtering options, the app will have the ability to filter by category, streamer, stream, 
      and by date. Additionally, I want to allow users to add multiple filters to allow for more flexibility. 
      For example, it may be useful if someone wanted to search for clips from four specific streamers and not 
      view the rest of the clips from a particular category.
    - Since the app would be focused on Twitch clips, there shouldn’t be an issue of the clips being hidden as 
      it will be the focus of the app.
    - When watching an app, I intend for the relevant clip information to be available under the video player, 
      with the chatroom replay under that, and a button that links to the Twitch livestream under that. (This 
      can be seen in the UI mockup)
    - After finishing a clip, I intend for more clips from that streamer or category to be displayed. 


Question 7 - Your "elevator pitch" (should take no more than ~60 seconds to tell)
My app idea is to make the process of finding and watching clips on Twitch easier. For those who don’t know, 
Twitch is a popular livestreaming platform, and they have a feature where users can save five to sixty-second 
clips from livestreams. This is important because people who don’t have time to watch an entire livestream, can 
watch the highlights through the clips. The issue with the current apps to watch clips on a mobile device are 
the lack of filtering options making them not very useful when searching for a specific stream or streamer. My 
app would improve the user experience for finding and watching Twitch clips by offering more specific filtering 
options such as by category, streamer, stream, and date. This way viewers can fine-tune their clip viewing 
experience to what they are looking for.


Question 8 - List any technologies you are considering for your project; compare them to alternative options, 
and state which one(s) you find more promising and why
1. Twitch API
Since my app idea at its core revolves around Twitch, the best way to create an app that uses Twitch’s resources 
is to use their API. This allows me to get a list of clips based on the user’s filtering options and display all 
the important information regarding each clip. I can then embed the Twitch video player in my app as well as the 
chatroom replay. As I am focusing on only Twitch videos for this project, there are no alternatives to the Twitch 
API as it is the only way I can fetch the clips.

2. OAuth 2.0
To make use of a user’s preferences from their Twitch account, I will need to validate their account information 
using the OAuth 2.0 protocol which is standard for all Twitch account requests. There are no alternatives to this 
if my app is based on Twitch clips because Twitch only uses OAuth 2.0 for their authentication. Despite this, 
OAuth is a promising option as it will allow users to log in through Twitch from my app so I can then access their 
favorite categories and streamers. With this information, I can recommend clips that will more closely match their 
preferences.


Question 9 - Team
I intend to complete the project on my own.


Question 10 - Additional team members (list all team members' names, or NA if a solo project)
NA


Question 11 - I am submitting (check all that apply)
A web-drawn user interface (e.g., Balsamiq, Lucidchart, or similar)
The user interface is called UserInterface.png


Question 12 - One-vs-two semesters (plans)
One-semester project
