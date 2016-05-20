# Morse-via-flashlight
Android JAVA project to simply broadcast sentences via smartphone's flashlight.

When the sentence is being broadcasted by flashlight by one thread, second thread is responsible for 
displaying which character and which Morse code sign (long or short) is now broadcasted.

View is created dynamically. It depends on sentence length.

Displaying broadcasted Morse code signs uses spannable strings to add other style for one character in string. 
The style for broadcasted sign is different from antoher characters from spannable string.
