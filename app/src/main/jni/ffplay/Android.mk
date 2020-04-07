LOCAL_PATH := $(call my-dir)


#++ ffmpeg prebuilt static libraries

include $(CLEAR_VARS)
LOCAL_MODULE := avformat
LOCAL_SRC_FILES := lib/libavformat.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avcodec
LOCAL_SRC_FILES := lib/libavcodec.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avdevice
LOCAL_SRC_FILES := lib/libavdevice.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avfilter
LOCAL_SRC_FILES := lib/libavfilter.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swresample
LOCAL_SRC_FILES := lib/libswresample.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swscale
LOCAL_SRC_FILES := lib/libswscale.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avutil
LOCAL_SRC_FILES := lib/libavutil.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := postproc
LOCAL_SRC_FILES := lib/libpostproc.a
include $(PREBUILT_STATIC_LIBRARY)


#-- ffmpeg prebuilt static libraries



include $(CLEAR_VARS)

LOCAL_MODULE := ffplay

LOCAL_C_INCLUDES += $(LOCAL_PATH)/include \
                    $(LOCAL_PATH)/../src \
                    $(LOCAL_PATH)/../SDL2/include \
                    $(LOCAL_PATH)/../SDL2_ttf \
                    $(LOCAL_PATH)/../SDL2_gfx 
                    
                    
LOCAL_CFLAGS    := -Os -Wall -std=c11 -fPIC -D__ANDROID__  
LOCAL_CPPFLAGS  := -Os -Wall -std=c++11 -fPIC -D__ANDROID__
LOCAL_LDFLAGS   := -shared -L$(LOCAL_PATH)/lib


# build ffplay for android
LOCAL_SRC_FILES := ffplay.c cmdutils.c

LOCAL_SHARED_LIBRARIES := SDL2_ttf SDL2_gfx SDL2 

LOCAL_STATIC_LIBRARIES := avformat avdevice avfilter avcodec swresample swscale avutil postproc

LOCAL_LDLIBS := -lGLESv1_CM -llog -lz

include $(BUILD_SHARED_LIBRARY)





