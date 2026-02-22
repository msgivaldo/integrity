#include <jni.h>
#include <unistd.h>
#include <string.h>

JNIEXPORT jboolean JNICALL
Java_dev_givaldo_integrity_checker_device_root_RootedDeviceChecker_checkRootNative(
        JNIEnv *env, jobject thiz) {
    const char *paths[] = {
            "/system/bin/su",
            "/system/xbin/su",
            "/sbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            NULL
    };

    int i = 0;
    while (paths[i] != NULL) {
        if (access(paths[i], F_OK) == 0) {
            return JNI_TRUE;
        }
        i++;
    }

    return JNI_FALSE;
}