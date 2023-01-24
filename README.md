# xmarksthespot
X marks the spot mapping app
Durban tourism app

Developer:
YUSUF PARUK
email : yusufparuk895@gmail.com
call : 0818533435

specs:
applicationId "com.yusuf.myapplication"
        minSdk 25
        targetSdk 32
        versionCode 1
        versionName "1.0"

Project name = "xmarksthespot"

github:
https://github.com/Dentistkiller/xmarksthespot


default logins:
emails:
testing@gmail.com
newuser@gmail.com

password:
123456


to intall the app on your mobile device:
unzip the package and go to path:
\ClassMap\app\build\intermediates\apk\debug\app-debug.apk

to run on an emulator:
unzip the package
open android studio
click file -> open -> go to the location you saved the unzipped project
click on the project name and open
once the project is loaded click the play button to run on your emulator

APPLICATION_ID = com.example.classmap
VERSION_CODE = 1
VERSION_NAME = 1.0


user manual:
The app starts with a login screen where you login with your email and password

a forgot password button is present which when clicked prompts for email
once you submit your email , you will recieve an email to reset your password

click the register button to register a new user
you can register a new user by clicking the register button enter an email and password then confirm your password
 
the your are redirected to the login screen where you login with your email and password
click the login button to continue
user authntication is carried out through firebase authentication.

you are now brought to the main screen
the app name is displayed at the top 
the main screen is our map 
the map will open on the users current location 
the users location will be given by a roun marker which will move around as the device does 
the user can explore the map by scrollig with one finger
the user can zoom in and out by pinching or expanding 2 fingers
twisting with 2 fingers can also rotate the map which initially faces north
you can hit the compass icon in the top right to refocus on the north if needed
we can hit the recentre button to recenter the map back to our location

we have a search button indicated by a magnifying glass
you can tap this button to open a search function
here a user can search for points of interest or addresses 
and the search results will appear below
tapping on one of the results and the map will open up to the location
the location will be marked with a red marker 
a route will be genterated between the users current location and the destionation
the navigate button will turn blue once a route is generated
tapping the navigation button will open up directions to get there 
directions will be given verbally
users can mute the verbal cues
users can tap the top of the screen to recieve the directions in text format
an exclaimation button will be present for a user to report any issues on their journey eg: roadworks, traffic, etc.
the user can click the X to return to the map

a button containing a traffic light is also present on the map screen
tapping this button will display current traffic on the roads

tapping any location on the screen generates a route to that location
routes can be removed by cliking on the home button on the bottom menu

there are 3 filter buttons on the right hand side, clicking the first will filter by popular landmarks, the second will filter by modern landmarks and the third will filter by historical landmarks.
popular landmarks are displayed with a green marker, historical with a blue marker and modern with a purple marker.

when a filter is active search results only display for those landmarks.
tapping home when  filter is active returns you to the unfiltered map

we have a parking activity on the menu
ht e parking activity will have a button for saving a users current location when they park and this will be stored
when the find car button is tapped it pin points the location of a users car and a route is generated to it
reclicking thhe save location button will removed the saved park location

we have a place picker activity whcih user can use to identify an are, just move the pin above your chosen location and it will display details for that location.
once a location i chosen a user can hit the tick button and a rout will be generated, here a user can choose which mode of transport to use to reach the location.
a time to destination for each mode of transport weill also be displayed.


on the menu we also have an instructions tab
and a settings tab

the instructions tab will display a list of icons and colors along with their meanings

the settings tab has a heading saying settings
it has a switch to toogle metric system, if it is on the system is metric, if it is off the system will be imperial.
the users email is also displayed


lastly we have a logout button which will log a user out and return them to the login screen.


privacy policy:
https://www.freeprivacypolicy.com/live/bfbe3991-2a2b-4660-a5a7-2fd3de01e9f5


thank you for using my project.
:)
