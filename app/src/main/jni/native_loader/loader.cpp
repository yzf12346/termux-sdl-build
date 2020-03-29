/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <dlfcn.h>

#include <jni.h>
#include <android_native_app_glue.h>

#include "log.h"

#define TAG "native-loader"


#ifdef __cplusplus
extern "C" {
#endif


static char* get_app_pathname(char *conf, char *str, int size) {
    LOGI(TAG, "Loading config file %s\n", conf);
    
    FILE *fp = fopen(conf, "rb");
    if (fp != NULL) {
        char *pathname = fgets(str, size, fp);
        fclose(fp);
        if (pathname != NULL) {
            return pathname;
        }
    }

    LOGE(TAG, "Can't open file %s\n", conf);
    return NULL;
}


/**
 * This is the main entry point of a native application that is using
 * android_native_app_glue.  It runs in its own thread, with its own
 * event loop for receiving input events and doing other things.
 */
void android_main(struct android_app *state) {
    
    char conf_dir[PATH_MAX];
    char buf[PATH_MAX];
    char *nativeApp = NULL;

    
    /**
     * Dummy function that used to be used to prevent the linker from stripping app
     * glue code. No longer necessary, since __attribute__((visibility("default")))
     * does this for us.
     * __attribute__((deprecated("Calls to app_dummy are no longer necessary. See "
               "https://github.com/android-ndk/ndk/issues/381."))) void
     */
     // Make sure glue isn't stripped.
    //app_dummy();

    JNIEnv *env = NULL;
    state->activity->vm->AttachCurrentThread(&env, 0);

    jobject me = state->activity->clazz;

    jclass acl = env->GetObjectClass(me); //class pointer of NativeActivity
    jmethodID giid = env->GetMethodID(acl, "getIntent", "()Landroid/content/Intent;");
    jobject intent = env->CallObjectMethod(me, giid); //Got our intent

    jclass icl = env->GetObjectClass(intent); //class pointer of Intent
    jmethodID gseid = env->GetMethodID(icl, "getStringExtra",
                                       "(Ljava/lang/String;)Ljava/lang/String;");

    jstring jparams = (jstring) env->CallObjectMethod(intent, gseid,
                                                       env->NewStringUTF("nativeApp"));

    if (jparams) {
        const char *param = env->GetStringUTFChars(jparams, 0);

        LOGI(TAG, "nativeApp = %s\n", param);

        if (param != NULL) {
            nativeApp = strdup(param);
        }

        env->ReleaseStringUTFChars(jparams, param);
    }

    state->activity->vm->DetachCurrentThread();

    if (!nativeApp) {
        getcwd(buf, sizeof(buf));
        LOGI(TAG, "current path %s\n", buf);

        snprintf(conf_dir, sizeof(conf_dir), "%s/tmpdir/native_loader.conf", buf);
        nativeApp = get_app_pathname(conf_dir, buf, sizeof(buf));
        if (!nativeApp) {
            LOGW(TAG, "Fail-safe mode...\n");
            nativeApp = get_app_pathname(
                    "/data/user/0/com.termux.sdl/tmpdir/native_loader.conf", buf,
                    sizeof(buf));
            if (!nativeApp) {
                LOGE(TAG, "Can't load native_loader.conf!\n");
                return;
            }
        }
    }

    void *handle = NULL;
    // new main function
    void (*new_main)(struct android_app *state);
    const char *error = NULL;

    LOGI(TAG, "Load native activity %s\n", nativeApp);

    handle = dlopen(nativeApp, RTLD_LAZY);
    if (!handle) {
        LOGE(TAG, "%s\n", dlerror());
        return;
    }

    dlerror();

    *(void **) (&new_main) = dlsym(handle, "android_main");
    if ((error = dlerror()) != NULL) {
        LOGE(TAG, "%s\n", error);
        return;
    }

    LOGI(TAG, "native app start...\n");

    (*new_main)(state);

    dlclose(handle);
}


#ifdef __cplusplus
};
#endif /* __cplusplus */
