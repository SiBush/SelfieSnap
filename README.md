== README

This is an android game for sending and recieving self destructing images. Player 1 is prompted to make a specific ridiculous/random face, and then Player 2 has 10 seconds to view the image before he has to guess what face Player 1 was trying to make. 

It is currently under development, so don't look for it in the google play store yet!


== To Do List

* Remove the "no new messages" and "no friends yet" textviews for users with messages/friends

* fix error where Toast messages don't seem to be showing? 

* create a face suggestions array and randomly assign faces along with photos (this can be done in the toast?)

* send face suggestion to Parse

* allow user to guess face from a randomized list (from the array?) that includes the true value (after the activity times out?)

* support video?

* drop the default camera app and allow users to draw on the image before it is send, add text overlays, and instagram filters like Snapchat


== Tech details

* Targets Android 4.0

* Uses apache commons io 2.4 to convert image files so that Parse can understand them

* Uses Picasso 2.3.4 to recieve/display images from Parse url

* Uses a Parse BaaS to allow user sign ups, friends, and sending of messages.

