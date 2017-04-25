# Android-Manual-Build-Command-Line
Sample Code on the creation of Android APKs using command line &amp; makefile only.

This is a sample/example code on how to create an android project with the command line only. 
- No use of ```gradle, ant, maven``` and all that other stuff.

# Description
I prepared 2 samples:

- **hello-jni:** This is the same demo as the one you find in the [android-ndk official sample repository](https://github.com/googlesamples/android-ndk). However, with the difference that I do not use gradle.

- **native-activity:** This should be the more interesting sample because I completely bypass having Java code. That means .cpp wrappers are not needed nor `System.loadLibrary("hellojni");` calls from Java. The entry point for the program is in the .cpp file like a regular .cpp software. It is a little bit difficult to understand what is going on because you have to take care of the main loop, the rendering and querying inputs yourself. It is more low-level but I think you want that kind of control.

# Why
- Your want full flexibility and know what you do.
- You not need most of gradles services
- You do not want bloat your project.
- 15+ seconds build time via gradle is unacceptable.

# Dependencies (aka what I was using)
- `android built-tools 25.0.3`
- `andrioid ndk r14b`
- `Java JDK 1.8.0_131` (**dex has issues with Java 1.8 for the moment, but we can compile Java 1.7 compatible code.**)

# How-To Build & Run
Have a look at the Makefile. Just run it with your individual paths.

To build & run in terminal:
```
mkdir ./build 
cd ./build
cmake ../
make {HelloJNI, NativeActivity}
adb logcat *:W
```

# Contact

Feel free to contact me if you have questions or just want to chat about it.

