cmake_minimum_required(VERSION 3.3)
project(NativeActivity)

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

set(SOURCE_FILES_CPP
        )

set(SOURCE_FILES_H
        )


set(CMAKE_VERBOSE_MAKEFILE 0)

add_custom_target(deploy
        COMMAND ${ANDROID_HOME}/platform-tools/adb install -r ${HOME_DIR}/bin/NativeActivity.apk
        COMMAND ${ANDROID_HOME}/platform-tools/adb logcat -c
        COMMAND ${ANDROID_HOME}/platform-tools/adb logcat *:E
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Deploying apk.")

add_custom_target(build
        DEPENDS bin/NativeActivity.apk
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Building apk.")

add_custom_target(dummy
        DEPENDS ${HOME_DIR}/lib/arm64-v8a/libnative-activity.so
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Doing dummy stuff.")


add_custom_command(OUTPUT ${HOME_DIR}/bin/NativeActivity.apk
        COMMAND ${BUILD_TOOLS}/zipalign -f 4 bin/NativeActivity.signed.apk ${HOME_DIR}/bin/NativeActivity.apk
        DEPENDS bin/NativeActivity.signed.apk
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating bin/NativeActivity.apk.")

add_custom_command(OUTPUT ${HOME_DIR}/bin/NativeActivity.signed.apk
        COMMAND $(JAVA_HOME)/bin/jarsigner -keystore ToyKey.keystore -storepass armena -keypass armena
        -signedjar ${HOME_DIR}/bin/NativeActivity.signed.apk bin/NativeActivity.unsigned.apk NativeActivityKey
        DEPENDS bin/NativeActivity.unsigned.apk ToyKey.keystore
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating bin/NativeActivity.signed.apk.")

add_custom_command(OUTPUT ${HOME_DIR}/bin/NativeActivity.unsigned.apk
        COMMAND ${BUILD_TOOLS}/aapt package -f -M AndroidManifest.xml -S res
        -I ${ANDROID_HOME}/platforms/android-25/android.jar -F ${HOME_DIR}/bin/NativeActivity.unsigned.apk bin
        COMMAND ${BUILD_TOOLS}/aapt add ${HOME_DIR}/bin/NativeActivity.unsigned.apk lib/arm64-v8a/libnative-activity.so
        DEPENDS bin/classes.dex lib/arm64-v8a/libnative-activity.so
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating bin/NativeActivity.unsigned.apk.")

add_custom_command(OUTPUT ${HOME_DIR}/bin/classes.dex
        COMMAND ${BUILD_TOOLS}/dx --dex --output=${HOME_DIR}/bin/classes.dex ${HOME_DIR}/obj
        DEPENDS obj/com/example/native_activity/R.class
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating bin/classes/dex.")

add_custom_command(OUTPUT ${HOME_DIR}/obj/com/example/native_activity/R.class
        COMMAND $(JAVA_HOME)/bin/javac -source 1.7 -target 1.7 -d obj -classpath ${ANDROID_HOME}/platforms/android-25/android.jar -sourcepath src src/com/example/native_activity/R.java
        DEPENDS src/com/example/native_activity/R.java
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating obj/com/example/native_activity/R.class.")

add_custom_command(OUTPUT ${HOME_DIR}/src/com/example/native_activity/R.java
        COMMAND ${BUILD_TOOLS}/aapt package -f -m  -S res -J src -M AndroidManifest.xml -I ${ANDROID_HOME}/platforms/android-25/android.jar
        DEPENDS res/values/strings.xml
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating src/com/example/native_activity/R.java.")

add_custom_command(OUTPUT ${HOME_DIR}/ToyKey.keystore
        COMMAND $(JAVA_HOME)/bin/keytool -genkeypair -validity 1000 -dname "CN=some company,O=Android,C=JPN" -keystore ${HOME_DIR}/ToyKey.keystore
        -storepass armena -keypass armena -alias NativeActivityKey -keyalg RSA
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating ToyKey.keystore.")



add_custom_command(OUTPUT ${HOME_DIR}/jni/libnative_app_glue.a
        COMMAND ${CC} -Wall -march=armv8-a -c ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c -o jni/native_app_glue.o
        COMMAND ${AR} rcs ${HOME_DIR}/jni/libnative_app_glue.a jni/native_app_glue.o
        BYPRODUCTS jni/native_app_glue.o
        DEPENDS ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating native_app_glue static library.")

add_custom_command(OUTPUT ${HOME_DIR}/lib/arm64-v8a/libnative-activity.so 
        COMMAND ${CXX} -Wall -I ${CXX_INCLUDES} -march=armv8-a --sysroot=${TOOLCHAIN}/sysroot -std=gnu++11 -fPIC -c jni/main.cpp -o jni/main.o
        COMMAND ${CXX} -I ${CXX_INCLUDES} -L jni -shared jni/main.o -o lib/arm64-v8a/libnative-activity.so -llog -landroid -lEGL -lGLESv1_CM -lnative_app_glue
        BYPRODUCTS jni/main.o
        DEPENDS jni/main.cpp jni/libnative_app_glue.a
        WORKING_DIRECTORY ${HOME_DIR}
        COMMENT "Creating c++ shared libraries.")