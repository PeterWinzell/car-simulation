copy /y opt\oculus.jar lib\oculus.jar
ECHO OFF
CLS
:MENU
ECHO.
ECHO ============== OpenDS 4.5 ==============
ECHO ...............................................
ECHO TO START SIMULATION PRESS 1 or 2 to select suitable settings, or 'Q' to EXIT.
ECHO ...............................................
ECHO.
ECHO 1 - Start OpenDS with default settings
ECHO 2 - Start OpenDS with optimized settings, suits for Countryside2 type of tasks
ECHO Q - QUIT
ECHO.
SET /P M=Type 1, 2, or Q then press ENTER:
IF %M%==1 GOTO DEFAULT
IF %M%==2 GOTO OPTIMIZED
IF %M%==Q || %M%==q GOTO :EOF

CLS

ECHO ============== INVALID INPUT ==============
ECHO -------------------------------------------
ECHO Please select a number from the Main Menu
ECHO [1-2] or select 'Q' to quit.
ECHO -------------------------------------------
ECHO ====== PRESS ANY KEY TO CONTINUE ==========

PAUSE > NUL
GOTO MENU 


ELSE GOTO MENU
:DEFAULT
start "OpenDS 4.5" /Realtime java -Xmx4096m -jar "OpenDS.exe" && exit
:OPTIMIZED
start "OpenDS 4.5" /Realtime java -Xms2048m -Xmx2048m -XX:NewRatio=3 -Xss1024m -XX:+UseParallelGC -XX:MaxGCPauseMillis=100 -XX:ParallelGCThreads=13 -jar "OpenDS.exe" && exit

