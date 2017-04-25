
#include <string.h>
#include <jni.h>

extern "C" 
jstring
Java_com_example_hellojni_HelloJNI_stringFromJNI( JNIEnv* env, jobject thiz);

JNIEXPORT jstring Java_com_example_hellojni_HelloJNI_stringFromJNI( JNIEnv* env, jobject thiz ){

    return env->NewStringUTF("Hello from JNI !  Compiled with ABI.");
}