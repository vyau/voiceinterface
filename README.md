# voiceinterface
voice recognition and text to speech

Dependencies:

   -- Maven build tool (on Ubuntu, apt-get install maven)
   -- Recent version of Java (Download from Oracle Java website, I am using Java 8)  
   -- vlc media player (on Ubuntu, apt-get install vlc)
   -- wget (on Ubuntu, apt-get install wget)


To build:

   -- cd to the directory with pom.xml,  type "mvn -amd install"
  
To run:

   java -cp sphinx4-samples-1.0-SNAPSHOT.jar -cp ./classes -cp sphinx4-samples-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.sphinx.demo.dialog.DialogDemo


To clean:

   mvn -amd clean


