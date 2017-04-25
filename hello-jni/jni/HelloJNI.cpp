
#include <string.h>
#include <jni.h>


jstring
Java_com_example_hellojni_HelloJNI_stringFromJNI( JNIEnv* env, jobject thiz ){

    return env->NewStringUTF("Hello from JNI !  Compiled with ABI.");
}