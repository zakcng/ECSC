#include <jni.h>
#include <stdio.h>
#include "main_HelloWorld.h"

JNIEXPORT void JNICALL   Java_main_HelloWorld_print(JNIEnv *env, jobject obj)  {
     printf("Hello World!\n");
     return;
}
