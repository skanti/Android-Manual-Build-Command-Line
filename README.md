# Android-Manual-Build-Command-Line
Sample Code on the creation of Android APKs using command line &amp; makefile only.

This is a sample/example code on how to create an android project with the command line only. 
- No use of ```gradle, ant, maven``` and all that other stuff.

# Description
I prepared 2 samples:

- **hello-jni:** This is the same demo as the one you find in the [android-ndk official sample repository](https://github.com/googlesamples/android-ndk). However, with the difference that I do not use gradle.

- **native-activity:** This should be the more interesting sample because I completely bypass having Java code - **no Java UI**. That means `c++` wrappers are not needed nor `System.loadLibrary("hellojni");` calls from Java. The entry point (`main`) for the program is in the `.cpp` file like a regular `c++` software. It is a little bit difficult to understand what is going on because you have to take care of the main loop, the rendering and querying inputs yourself. It is more low-level but I think you want that kind of control. Again, no gradle involved.

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
Note that in the `CMakeLists.txt` file you have to update the paths with your individual paths. That means, you have to download a bunch of software, place them in a folder and set up the native standalone toolchain from the android ndk.

The command to create the [standalone-toolchain](https://developer.android.com/ndk/guides/standalone_toolchain.html) is

```
$NDK/build/tools/make_standalone_toolchain.py --arch arm64 --api 24 --install-dir some_folder
```
Note that `api 25` is not available with `ndk-r14b` because there are no new features since version `24` (android-ndk guys told me).


To build & run in terminal:
```
cd {hello-jni, native-activity}
mkdir ./build 
cd ./build
cmake ../
make {HelloJNI, NativeActivity}
adb logcat *:W
```

The curly braces `{A,B}` mean, you have to choose for one. E.g. `cd {A, B}` means decide for `cd A` or `cd B`.

If everything goes right, your build will look like this:

![good-build](http://i.imgur.com/dqXF0lE.png)

# To-Do

- [x] have `CMake` as build-system
- [ ] remove `Android.mk` makefiles. Instead, compile everything by hand. This will give so much more control (no more hidden compilation flags, etc.)

# Contact

Feel free to contact me if you have questions or just want to chat about it.

