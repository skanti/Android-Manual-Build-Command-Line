cmake_minimum_required(VERSION 3.0)
project(HelloJNI)

# -> dirs
set(TOOLCHAIN "/home/amon/software/android/native-toolchain")
set(ANDROID_HOME "/home/amon/software/android")
set(BUILD_TOOLS "/home/amon/software/android/build-tools/25.0.3")
set(ANDROID_NDK "/home/amon/software/android/ndk/ndk-r14b")
set(JAVA_HOME $ENV{JAVA_HOME})
# <-

# -> compiler
set(CC ${TOOLCHAIN}/bin/aarch64-linux-android-gcc)
set(CXX ${TOOLCHAIN}/bin/aarch64-linux-android-c++)
set(AR ${TOOLCHAIN}/bin/aarch64-linux-android-ar)
set(CXX_FLAGS "")
set(CXX_INCLUDES "${ANDROID_NDK}/sources/android/native_app_glue")
# <-

set(HOME_DIR ${CMAKE_CURRENT_SOURCE_DIR})

set(CMAKE_VERBOSE_MAKEFILE 1)

add_custom_target(HelloJNI
        #COMMAND rm -rf bin/*
        COMMAND ${ANDROID_HOME}/platform-tools/adb install -r ${HOME_DIR}/bin/HelloJNI.apk
        DEPENDS bin/HelloJNI.apk
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Installing apk.")


add_custom_command(OUTPUT ${HOME_DIR}/bin/HelloJNI.apk
        COMMAND ${BUILD_TOOLS}/zipalign -f 4 bin/HelloJNI.signed.apk bin/HelloJNI.apk
        DEPENDS bin/HelloJNI.signed.apk
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating bin/HelloJNI.apk.")

add_custom_command(OUTPUT ${HOME_DIR}/bin/HelloJNI.signed.apk
        COMMAND $(JAVA_HOME)/bin/jarsigner -keystore ToyKey.keystore -storepass armena -keypass armena
        -signedjar ${HOME_DIR}/bin/HelloJNI.signed.apk bin/HelloJNI.unsigned.apk HelloJNIKey
        DEPENDS bin/HelloJNI.unsigned.apk ToyKey.keystore
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating bin/HelloJNI.signed.apk.")

add_custom_command(OUTPUT ${HOME_DIR}/bin/HelloJNI.unsigned.apk
        COMMAND ${BUILD_TOOLS}/aapt package -f -M AndroidManifest.xml -S res
        -I ${ANDROID_HOME}/platforms/android-25/android.jar -F bin/HelloJNI.unsigned.apk bin
        COMMAND ${BUILD_TOOLS}/aapt add bin/HelloJNI.unsigned.apk lib/arm64-v8a/libhellojni.so
        DEPENDS bin/classes.dex lib/arm64-v8a/libhellojni.so
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating bin/HelloJNI.unsigned.apk.")

add_custom_command(OUTPUT ${HOME_DIR}/bin/classes.dex
        COMMAND ${BUILD_TOOLS}/dx --dex --output=${HOME_DIR}/bin/classes.dex ${HOME_DIR}/obj
        DEPENDS obj/com/example/hellojni/HelloJNI.class
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating bin/classes/dex.")

add_custom_command(OUTPUT ${HOME_DIR}/obj/com/example/hellojni/HelloJNI.class
        COMMAND $(JAVA_HOME)/bin/javac -source 1.7 -target 1.7 -d obj -classpath ${ANDROID_HOME}/platforms/android-25/android.jar 
        -sourcepath src src/com/example/hellojni/R.java src/com/example/hellojni/HelloJNI.java
        DEPENDS src/com/example/hellojni/R.java src/com/example/hellojni/HelloJNI.java
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating obj/com/example/hellojni/HelloJNI.class.")

add_custom_command(OUTPUT ${HOME_DIR}/src/com/example/hellojni/R.java
        COMMAND ${BUILD_TOOLS}/aapt package -f -m  -S res -J src -M AndroidManifest.xml -I ${ANDROID_HOME}/platforms/android-25/android.jar
        DEPENDS res/values/strings.xml
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating src/com/example/hellojni/R.java.")

add_custom_command(OUTPUT ${HOME_DIR}/ToyKey.keystore
        COMMAND $(JAVA_HOME)/bin/keytool -genkeypair -validity 1000 -dname "CN=some company,O=Android,C=JPN" -keystore ${HOME_DIR}/ToyKey.keystore
        -storepass armena -keypass armena -alias HelloJNIKey -keyalg RSA
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating ToyKey.keystore.")


add_custom_command(OUTPUT ${HOME_DIR}/lib/arm64-v8a/libhellojni.so
        COMMAND ${CXX} -march=armv8-a --sysroot=${TOOLCHAIN}/sysroot -fPIC -c jni/HelloJNI.cpp -o jni/HelloJNI.o
        COMMAND ${CXX} -shared jni/HelloJNI.o -o ${HOME_DIR}/lib/arm64-v8a/libhellojni.so
        DEPENDS jni/HelloJNI.cpp
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating lib/arm64-v8a/libhellojni.so")