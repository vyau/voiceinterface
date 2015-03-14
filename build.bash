#!/bin/bash

## http://www.speech.cs.cmu.edu/tools/lmtool-new.html


javac -cp "./sphinx4-core-1.0-SNAPSHOT.jar:./sphinx4-data-1.0-SNAPSHOT.jar" src/main/java/org/voinput/dialog/DialogDemo.java


mkdir temp
cp *.jar temp
mkdir -p temp/org/voinput/dialog
cp src/main/java/org/voinput/dialog/DialogDemo.class temp/org/voinput/dialog
cp src/main/resources/org/voinput/dialog/* temp/org/voinput/dialog

cd temp
jar xvf sphinx4-core-1.0-SNAPSHOT.jar
jar xvf sphinx4-data-1.0-SNAPSHOT.jar
rm *.jar
jar cvf voinput_single.jar *
mv voinput_single.jar ..
cd ..
rm -fr temp

## to run --  java -cp voinput_single.jar org.voinput.dialog.DialogDemo



