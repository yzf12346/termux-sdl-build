LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ffplay

LOCAL_C_INCLUDES += ../SDL2/include \
                    $(LOCAL_PATH)/../src \
                    $(LOCAL_PATH)/include

LOCAL_CXX_FLAGS  := -std=c++11 -DANDROID

# build ffplay for android
LOCAL_SRC_FILES := ffplay.c cmdutils.c

LOCAL_SHARED_LIBRARIES := SDL2 

LOCAL_STATIC_LIBRARIES += libavformat libavdevice libavfilter libavcodec libswresample libswscale libavutil libpostproc

LOCAL_LDLIBS := -lGLESv1_CM -llog -lz

include $(BUILD_SHARED_LIBRARY)



#++ ffmpeg prebuilt static libraries

include $(CLEAR_VARS)
LOCAL_MODULE := libavformat
LOCAL_SRC_FILES := lib/libavformat.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavcodec
LOCAL_SRC_FILES := lib/libavcodec.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavdevice
LOCAL_SRC_FILES := lib/libavdevice.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavfilter
LOCAL_SRC_FILES := lib/libavfilter.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libswresample
LOCAL_SRC_FILES := lib/libswresample.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libswscale
LOCAL_SRC_FILES := lib/libswscale.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavutil
LOCAL_SRC_FILES := lib/libavutil.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libpostproc
LOCAL_SRC_FILES := lib/libpostproc.a
include $(PREBUILT_STATIC_LIBRARY)


#-- ffmpeg prebuilt static libraries
