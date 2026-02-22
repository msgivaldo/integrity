#include <jni.h>
#include <stdio.h>
#include <string.h>

JNIEXPORT jboolean JNICALL
Java_dev_givaldo_integrity_checker_virtualization_VirtualLibraryChecker_isVirtualLibraryPresent(JNIEnv *env, jobject thiz) {
    FILE *fp;
    char line[512];

    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL) {
        return JNI_FALSE;
    }

    while (fgets(line, sizeof(line), fp)) {
        // "libva" -> VirtualApp
        // "libvxp" -> VirtualXposed
        // "libsandbox" -> Sandboxes comuns
        // "libapkparse" -> cloners
        if (strstr(line, "libva.so") ||
            strstr(line, "libva-native.so") ||
            strstr(line, "libvxp.so") ||
            strstr(line, "libsandbox.so")) {

            return JNI_TRUE;
        }
    }

    fclose(fp);
    return JNI_FALSE;
}