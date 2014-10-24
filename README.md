== README

This is an android game for sending and recieving self destructing images. Player 1 is prompted to make a specific ridiculous/random face, and then Player 2 has 10 seconds to view the image before he has to guess what face Player 1 was trying to make. 

It is currently under development, but feel free to check out the [early alpha release](https://play.google.com/store/apps/details?id=com.johncorser.selfiesnap)!


== To Do List

* support video?

* drop the default camera app and allow users to draw on the image before it is send, add text overlays, and instagram filters like Snapchat

* Speed up the app by adding background threads

* Improve UX with more "loading" spinners.


== Tech details

* Targets Android 4.0

* Uses apache commons io 2.4 to convert image files so that Parse can understand them

* Uses Picasso 2.3.4 to recieve/display images from Parse url

* Uses a Parse BaaS to allow user sign ups, friends, and sending of messages.

