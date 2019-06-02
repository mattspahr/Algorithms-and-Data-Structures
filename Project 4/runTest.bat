@echo off 
REM Script to set the classpath for running tests of this project.

:build
call mvn clean install
REM extract the dependent jars so we can add them to our classpath later
cd target
jar -xvf proj0-csi403.war
cd ..


:run
REM build up the classpath 
REM arg1 is the name of the class to run, including the full package name (e.g. csi403.MyClass)
call java -cp "target/classes;target/WEB-INF/lib/*" %1 %2 %3 %4 %5 %6 %7 %8 %9 


