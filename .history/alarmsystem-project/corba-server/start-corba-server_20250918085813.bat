@echo off
echo Starting CORBA Alarm Server...
cd /d "%~dp0"

REM Check if Java is available
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not available in PATH
    pause
    exit /b 1
)

REM Check if classes are compiled
if not exist "target\classes\alarmsystem\Server.class" (
    echo ERROR: Server.class not found. Please run 'mvn compile' first.
    pause
    exit /b 1
)

echo Starting CORBA server...
echo Press Ctrl+C to stop the server
java -cp target\classes alarmsystem.Server

pause