#ifndef __LOG_H__
#define __LOG_H__


#include <android/log.h>

#define LOG_TAG "System.out"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__) 
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__) 
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__) 
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__) 
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL, LOG_TAG, __VA_ARGS__) 



#define printf(fmt, ...)  \
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, \
    "\e[35m[%s:%s:\e[32mline:%d]\e[0m\t" fmt, \
    __FILE__, __FUNCTION__, __LINE__, ##__VA_ARGS__);

#endif // __LOG_H__ 
