# Android-Manual-Build-Command-Line
Sample Code on the creation of Android APKs using command line &amp; makefile only.

# Description
This is a sample/example code on how to create an android project with the command line only. 
- No use of ```gradle, ant, maven, android.mk``` and all that other stuff.

# Why
- Your want full flexibility and know what you do.
- You not need 99% of gradles services
- You do not want bloat your project.
- 15+ seconds build time via gradle is unacceptable.

# Dependencies
- ```android-sdk 24.4.1_1```
- ```android built-tools 23.0.3```
- ```Java JDK 1.7.0_79``` (**Android has issues with Java 1.8 for the moment.**)

# How-To Build & Run
Have a look at the Makefile. Just run it with your individual paths.

To build & run in terminal:
```
make
adb install -r ./bin/hellojni.apk
adb logcat *:W
```

# Contact

Feel free to contact me if you have questions or just want to chat about it.

