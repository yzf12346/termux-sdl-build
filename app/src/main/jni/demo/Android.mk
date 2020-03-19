LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := main

SDL_PATH := ../SDL2

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../SDL2/include 

LOCAL_CXX_FLAGS  := -std=c++14 -DANDROID

# Add your application source files here...
LOCAL_SRC_FILES := ../SDL2/src/main/android/SDL_android_main.c \
	                   testgles.c

LOCAL_SHARED_LIBRARIES := SDL2 

LOCAL_LDLIBS := -lGLESv1_CM -llog

include $(BUILD_SHARED_LIBRARY)
