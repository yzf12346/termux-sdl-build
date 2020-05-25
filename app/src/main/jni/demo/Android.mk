LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := main

LOCAL_C_INCLUDES += ../SDL2/include 

LOCAL_CXX_FLAGS  := -fPIC -std=c++11 -DANDROID

# Add your application source files here...
LOCAL_SRC_FILES := ../SDL2/src/main/android/SDL_android_main.c \
	               testgles.c

LOCAL_SHARED_LIBRARIES := SDL2 

LOCAL_LDLIBS := -lGLESv1_CM -llog -lz

include $(BUILD_SHARED_LIBRARY)




