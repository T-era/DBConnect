@echo off

set APPLICATION_ROOT=D:\Java\workspace2\DBConnect3.2
set APPLICATION_LIB=%APPLICATION_ROOT%\lib
set EXTEND_LIB=D:\Java\workspace2\libs
set CLASSPATH=%CLASSPATH%;./bin/;%APPLICATION_LIB%\commons-betwixt-0.7.jar;%APPLICATION_LIB%\commons-beanutils.jar;%APPLICATION_LIB%\commons-digester.jar;%APPLICATION_LIB%\commons-logging.jar;%APPLICATION_LIB%\YtelUtility.jar;%APPLICATION_LIB%\YtelVersionViewer.jar;%EXTEND_LIB%\sqljdbc4.jar;%EXTEND_LIB%\ojdbc6.jar

set CLASSPATH=%CLASSPATH%;%APPLICATION_LIB%\mysql-connector-java-5.1.8-bin.jar

cd %APPLICATION_ROOT%
start java bin.DBConnect
