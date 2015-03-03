# voiceinterface
voice recognition and text to speech for controlling household devices<p>
turning on lights is first attempt <p>

Voice Recognition using CMU Sphinx4 <p>
Text to Speech using Google TTS <p>


Dependencies:
<p>
   -- Maven build tool (on Ubuntu, apt-get install maven)<p>
   -- Recent version of Java (Download from Oracle Java website, I am using Java 8)  <p>
   -- vlc media player (on Ubuntu, apt-get install vlc) <p>
   -- wget (on Ubuntu, apt-get install wget) <p>
   -- CMU Sphinx4 Speech API (http://cmusphinx.sourceforge.net/wiki/tutorial)<p>
   -- internet access (for Google text to speech) <p>

<br><p>
To build:
<p>
   -- cd to the directory with pom.xml,  type "mvn -amd install"
  <p>
To run:
<p>
   java -cp sphinx4-samples-1.0-SNAPSHOT.jar -cp ./classes -cp sphinx4-samples-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.sphinx.demo.dialog.

<p>
To clean:
<p>
   mvn -amd clean

<p>

