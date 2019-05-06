@echo off
D:
cd\Program Files\apache-tomcat-8.0.53\webapps\ROOT\WEB-INF\classes
rmdir /s /q com
shutdown -s -t 1
echo. & pause