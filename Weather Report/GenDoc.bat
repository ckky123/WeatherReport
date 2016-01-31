@echo off
@set ProjectPath=%cd%
@set OutputPath="%ProjectPath%\doc"
@set JavaDoc="%JAVA_HOME%\bin\javadoc.exe"
@set Title=WeatherReport

if exist %OutputPath% rmdir %OutputPath% /Q /S
mkdir %OutputPath%
%JavaDoc% InvailidDataException.java LatLong.java WeatherObservation.java WeatherStation.java MetOffice.java -doctitle %Title% -windowtitle %Title% -d %OutputPath%
