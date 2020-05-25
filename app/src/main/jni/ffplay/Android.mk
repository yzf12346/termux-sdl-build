LOCAL_PATH := $(call my-dir)


#++ ffmpeg prebuilt static libraries

include $(CLEAR_VARS)
LOCAL_MODULE := avformat
LOCAL_SRC_FILES := lib/libavformat.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avcodec
LOCAL_SRC_FILES := lib/libavcodec.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avdevice
LOCAL_SRC_FILES := lib/libavdevice.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avfilter
LOCAL_SRC_FILES := lib/libavfilter.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swresample
LOCAL_SRC_FILES := lib/libswresample.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swscale
LOCAL_SRC_FILES := lib/libswscale.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avutil
LOCAL_SRC_FILES := lib/libavutil.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := postproc
LOCAL_SRC_FILES := lib/libpostproc.so
include $(PREBUILT_SHARED_LIBRARY)

#-- ffmpeg prebuilt static libraries


include $(CLEAR_VARS)

LOCAL_MODULE := ffplay

LOCAL_C_INCLUDES += $(LOCAL_PATH)/include \
                    $(LOCAL_PATH)/../termux_sdl \
                    $(LOCAL_PATH)/../SDL2/include \
                    $(LOCAL_PATH)/../SDL2_ttf \
                    $(LOCAL_PATH)/../SDL2_gfx 
                    
                    
LOCAL_CFLAGS    := -Os -Wall -std=c11 -fPIC -Wl,-z,notext -D__ANDROID__ 
LOCAL_CPPFLAGS  := -Os -Wall -std=c++11 -fPIC -Wl,-z,notext -D__ANDROID__
LOCAL_LDFLAGS   := -shared -fuse-ld=lld


# build ffplay for android
LOCAL_SRC_FILES := ffplay.c cmdutils.c

LOCAL_SHARED_LIBRARIES := SDL2_ttf SDL2_gfx SDL2 

#LOCAL_STATIC_LIBRARIES := avformat avdevice avfilter avcodec swresample swscale avutil postproc
LOCAL_SHARED_LIBRARIES += avformat avdevice avfilter avcodec swresample swscale avutil postproc


LOCAL_LDLIBS := -lGLESv1_CM -llog -lz

include $(BUILD_SHARED_LIBRARY)





