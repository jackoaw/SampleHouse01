# SampleHouse01

This is a sample project for a smarthome based on the Amazon Alexa Skill development sample repo

Prequesites:
[Need to set jks ssl certificate in pom.xml

How to run:
Go to root directory of project

Compile: mvn assembly:assembly -DdescriptorId=jar-with-dependencies package

Run: mvn exec:java -Dexec.executable=”java”
