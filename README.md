

  This is a demo program of how to act a generic voice input using SPhinx 4 voice recognition library.<br>
  http://cmusphinx.sourceforge.net/wiki/tutorialsphinx4 <br>
  Please refer to the included Sphinx4 licensing term file. <br>
 
  I need a generic voice input mechanism to work with a very specific language model so i can
  use voice to drive home automation tasks.   
  A few items I have planned:    
  <br>   Turning on/off X10 light switches, manage Skype interactions (e.g. dialing, auto-answer calls)
 <br>
  A lot of these tasks need to be in their own event loop.  Hence, I am making this class
  a singleton thread class, to yield control to the caller and have voiceinput listening in the
  background.
  <br>
<p>

File Listings<br>

<ul>
<li> build.bash -- very simple built script 
<li> src/main/java/org/voinput/dialog/DialogDemo.java -- code logic for voice input
<li> src/main/resources/org/voinput/dialog/reference.txt (and reference.lm) -- this is the language 
     model for my application.
     Feel free to modify it to your need.  Then, from Sphinx 4 manual, you can
     use this URL (http://www.speech.cs.cmu.edu/tools/lmtool-new.html) to generate a new reference.lm.
<li> sphinx4-core...jar and sphinx4-data...jar -- These are from the Sphinx4 library 
     and necessary for this demo to compile.

