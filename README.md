# Android Developer Nanodegree Program | Capstone Project
Blends was made as part of Udacity's Android Developer Nanodegree Program. Blends offers users the possibility to search for small, independent coffee shops in the city they are living (only available in London at the moment). What makes this different from all other coffee shop apps is that the cafes displayed are unique, hippie and offer a great caffeinated beverages for anyone who feels adventurous and wishes to experience new tastes.

# Features
- Explore the map for cafes in your area 
- Access details for each cafe, including contact details and opening hours
- Add cafes either to favourites or on the 'wish-to-visit' list
- Homescreen widget with cafe of the day

![](screenshot_group.png)

# Common Project Requirements
- Project is made up of two stages: the design stage and the implementation stage 
- App conforms to common standards found in the [Android Nanodegree General Project Guidelines](http://udacity.github.io/android-nanodegree-guidelines/core.html)
- App is written solely in the Java Programming Language

# Core Platform Development
- App integrates a third-party library
- App validates all input from servers and users. If data does not exist or is in the wrong format, the app logs this fact and does not crash
- App keeps all strings in a strings.xml file and enables RTL layout switching on all layouts
- App provides a widget to provide relevant information to the user on the home screen

# Material Design
- App theme extends AppCompat
- App uses an app bar and associated toolbars
- App uses standard and simple transitions between activities

# Data Persistence
- App stores data locally either by implementing a ContentProvider OR using Firebase Realtime Database. No third party frameworks nor Room Persistence Library may be used
- If it regularly pulls or sends data to/from a web service or API, app updates data in its cache at regular intervals using a SyncAdapter or JobDispacter
- App uses a Loader to move its data to its views
