#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include "main_PasswordManager.h"

JNIEXPORT jint JNICALL   Java_main_PasswordManager_createFile(JNIEnv *env, jobject obj)  {
     printf("Create file function.\n");
     return 0;
}

JNIEXPORT jint JNICALL   Java_main_PasswordManager_addPass(JNIEnv *env, jobject obj, jstring chatName, jstring hashedPass)  {
     printf("Add pass function.\n");
     return 0;
}

JNIEXPORT jint JNICALL   Java_main_PasswordManager_removePass(JNIEnv *env, jobject obj, jstring chatName, jstring hashedPass)  {
     printf("Remove password function.\n");
     return 0;
}

JNIEXPORT jint JNICALL   Java_main_PasswordManager_validPass(JNIEnv *env, jobject obj, jstring chatName, jstring hashedPass)  {
     printf("Valid password function.\n");
     return 1;
}

JNIEXPORT jstring JNICALL   Java_main_PasswordManager_getSalt(JNIEnv *env, jobject obj, jstring chatName)  {
    char *saltStr = (char*)malloc(5);
    strcpy(saltStr, "salt"); // with the null terminator the string adds up to 10 bytes
    jstring jstrBuf = (*env)->NewStringUTF(env, saltStr);
    return jstrBuf;
}
