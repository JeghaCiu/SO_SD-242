@echo off
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
if not exist out mkdir out
javac -encoding UTF-8 -d out src\*.java
if errorlevel 1 exit /b 1
java -cp out SchedulerApp
