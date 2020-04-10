/*
 * Copyright (C) 2016 The Android Open Source Project
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

#include <jni.h>
#include <unistd.h>

#include "SDL.h"
#include "SDL_image.h"
#include "SDL_mixer.h"
#include "SDL_net.h"
#include "SDL_ttf.h"
#include "SDL2_gfxPrimitives.h"

#include "ffversion.h"

#include "log.h"

#define TAG "Termux_sdl"

#ifdef __cplusplus
extern "C" {
#endif

enum SDL_Libraries {
    SDL2,
    SDL2_IMAGE,
    SDL2_MIXER,
    SDL2_NET,
    SDL2_TTF,
    SDL2_GFX
};

 // fun setEnv()
JNIEXPORT jint JNICALL Java_com_termux_sdl_JNI_setEnv(JNIEnv *env, jobject thiz, 
                                jstring name, jstring value, jboolean overwrite){
    const char *_name  = env->GetStringUTFChars(name, NULL);
    const char *_value = env->GetStringUTFChars(value, NULL);
    LOGI(TAG, "name: %s\nvalue:.%s\n", _name, _value);
    return setenv(_name, _value, overwrite);
}

// fun unSetEnv()
JNIEXPORT jint JNICALL Java_com_termux_sdl_JNI_unSetEnv(JNIEnv *env, jobject thiz, 
                                                                    jstring name) {
    const char *_name = env->GetStringUTFChars(name, NULL);

    return unsetenv(_name);
}

// fun getEnv()
JNIEXPORT jstring JNICALL Java_com_termux_sdl_JNI_getEnv(JNIEnv *env, jobject thiz, 
                                                                    jstring name) {
    const char *_name = env->GetStringUTFChars(name, NULL);

    char *value = getenv(_name);
    if (!value) {
		return NULL;
    } else {
		return env->NewStringUTF(value);
    }
}

// fun chDir()
JNIEXPORT jint JNICALL Java_com_termux_sdl_JNI_chDir(JNIEnv *env, jobject thiz, 
                                                                  jstring path){
    const char *_path = env->GetStringUTFChars(path, NULL);
    return chdir(_path);
}


// fun getFFmpegVersion()
JNIEXPORT jstring JNICALL Java_com_termux_sdl_JNI_getFFmpegVersion(JNIEnv *env, 
                                                    jclass thiz) {
    return env->NewStringUTF(FFMPEG_VERSION);
}


// fun getSDLVersion()
JNIEXPORT jstring JNICALL Java_com_termux_sdl_JNI_getSDLVersion(JNIEnv *env, 
                                                    jclass thiz, jint lib) {
    char buf[256];
    SDL_version version;
    
    switch(lib) {
    case SDL2_IMAGE:
	    SDL_IMAGE_VERSION(&version);
	    break;
    case SDL2_MIXER:
	    SDL_MIXER_VERSION(&version);
	    break;
    case SDL2_NET:
	    SDL_NET_VERSION(&version);
	    break;
    case SDL2_TTF:
	    SDL_TTF_VERSION(&version);
	    break;
    case SDL2_GFX:
        SDL_GFX_VERSION(&version);
	    break;
    case SDL2:
    default:
	    SDL_GetVersion(&version);
	    break;
    }

    snprintf(buf, sizeof(buf), "%d.%d.%d", version.major, version.minor, version.patch);

    return env->NewStringUTF(buf);
}


#ifdef __cplusplus
}
#endif /* __cplusplus */

