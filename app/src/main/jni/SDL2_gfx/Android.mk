LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := SDL2_gfx


LOCAL_C_INCLUDES := -I../SDL2/include 

LOCAL_CXX_FLAGS  := -std=c++14 -DANDROID

LOCAL_SRC_FILES := SDL2_framerate.c \
	               SDL2_gfxPrimitives.c \
	               SDL2_imageFilter.c \
	               SDL2_rotozoom.c
	               

LOCAL_SHARED_LIBRARIES := SDL2 

LOCAL_LDLIBS := -lGLESv1_CM -llog

include $(BUILD_SHARED_LIBRARY)
