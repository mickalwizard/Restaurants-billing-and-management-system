@echo off
REM Compiles and runs the Restaurant Billing & Management System (Windows)
if not exist out mkdir out
javac -d out -encoding UTF-8 -sourcepath src\main\java src\main\java\restaurant\Main.java src\main\java\restaurant\model\*.java src\main\java\restaurant\service\*.java src\main\java\restaurant\util\*.java
if errorlevel 1 (
    echo.
    echo Compilation failed. Please check the errors above.
    pause
    exit /b 1
)
java -cp out restaurant.Main
pause
