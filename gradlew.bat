@rem
@rem Copyright 2015 the original author or authors.
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem SPDX-License-Identifier: Apache-2.0
@echo off
setlocal
set DIRNAME=%~dp0
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%
if defined JAVA_HOME (
    set JAVA_EXE=%JAVA_HOME%\bin\java.exe
) else (
    set JAVA_EXE=java.exe
)
"%JAVA_EXE%" -version >NUL 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
    exit /b 1
)
"%JAVA_EXE%" "-Xmx64m" "-Xms64m" "-Dorg.gradle.appname=%APP_BASE_NAME%" -jar "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" %*
