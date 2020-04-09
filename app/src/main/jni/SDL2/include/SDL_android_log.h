#ifndef SDL_android_h_
#define SDL_android_h_

#include "begin_code.h"

/* Set up for C function definitions, even when using C++ */
#ifdef __cplusplus
extern "C" {
#endif

// 这里只定义两个日志级别，两个够用了
enum log_priority {
    LOG_INFO, LOG_ERROR
};


// 打印日志到java层
extern DECLSPEC void SDLCALL SDL_AndroidLogPrint(int, const char*, const char*, ...);


/* Ends C function definitions when using C++ */
#ifdef __cplusplus
}
#endif
#include "close_code.h"

#endif /* SDL_android_h_ */

