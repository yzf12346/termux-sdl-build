# termux-sdl

这是一个termux sdl插件，为编译SDL2和native app程序，examples来源于cctools

### 如何使用：
解压libs.zip文件，复制SDL库文件到/data/data/com.termux/files/usr/lib

解压heades.zip文件，复制SDL头文件到/data/data/com.termux/files/usr/include

解压examples.zip文件到/sdcard根目录(showimage和playmus需要绝对路径)

```
# 进入examples的示例代码下，执行 make run
# 比如...
cd /sdcard/examples/SDL2/draw2
make run
```

 **** 

This is a termux sdl plugin, for compiling SDL2 and native app programs, examples from cctools

### How to use:
Extract the libs.zip file and copy the SDL library file to /data/data/com.termux/files/usr/lib

Extract the headers.zip file and copy the SDL header file to /data/data/com.termux/files/usr/include

Extract the examples.zip file to /sdacrd root directory (because showimage and playmus need absolute paths)


```
# Enter the demo code under examples and execute make run
# for example
cd /sdcard/examples/SDL2/draw2
make run
```


screenshot1.jpg
![image](https://raw.githubusercontent.com/Lzhiyong/termux-sdl/master/screenshots/screenshot1.jpg)


screenshot2.jpg
![image](https://raw.githubusercontent.com/Lzhiyong/termux-sdl/master/screenshots/screenshot2.jpg)


screenshot3.jpg
![image](https://raw.githubusercontent.com/Lzhiyong/termux-sdl/master/screenshots/screenshot3.jpg)


screenshot4.jpg
![image](https://raw.githubusercontent.com/Lzhiyong/termux-sdl/master/screenshots/screenshot4.jpg)


screenshot5.jpg
![image](https://raw.githubusercontent.com/Lzhiyong/termux-sdl/master/screenshots/screenshot5.jpg)


 **** 

修复SDL2默认不能打相对路径bug

Fix SDL2 Default can not open the relative path bug
```c
// SDL2/src/file/SDL_rwops.c

#if defined(__ANDROID__)
#ifdef HAVE_STDIO_H
    /* Try to open the file on the filesystem first */
    if (*file == '/') {
        // 打开文件，绝对路径
        __android_log_print(ANDROID_LOG_INFO, "SDL", "SDL2 open file open from external path: %s\n", file);
        FILE *fp = fopen(file, mode);
        if (fp != NULL) {
            return SDL_RWFromFP(fp, 1);
        }
    } else {
        // 打开文件，相对路径
        /* Try opening it from internal storage if it's a relative path */
        char *path = NULL;
        // pwd from TermuxSDLActivity JNI.setEnv();
        char *pwd = NULL;
        FILE *fp = NULL;
        

        /* !!! FIXME: why not just "char path[PATH_MAX];" ? */
        path = SDL_stack_alloc(char, PATH_MAX);
        if (path != NULL) {
            pwd = getenv("PWD");
            // 如果以相对路径的方式去打开文件，Android SDL2默认会加载内部路径下的文件
            // 也就是 /data/data/package_name/files/your_file
            // 在这里添加pwd的作用，是让SDL2默认加载 你自己的项目 目录下的文件
            if(pwd != NULL) {
                // pwd的值等于JNI.java setEnv("PWD", pwd, true)
                SDL_snprintf(path, PATH_MAX, "%s/%s", pwd, file);
            } else {
                // SDL_AndroidGetInternalStoragePath = /data/data/package_name/files
                SDL_snprintf(path, PATH_MAX, "%s/%s", SDL_AndroidGetInternalStoragePath(), file);
            }
            __android_log_print(ANDROID_LOG_INFO, "SDL", "SDL2 Open file from internal path: %s\n", path);
            
            fp = fopen(path, mode);
            SDL_stack_free(path);
            if (fp) {
                return SDL_RWFromFP(fp, 1);
            }
        }
    }
#endif /* HAVE_STDIO_H */

    /* Try to open the file from the asset system */
    rwops = SDL_AllocRW();
    
    if (!rwops) return NULL; /* SDL_SetError already setup by SDL_AllocRW() */
    
    __android_log_print(ANDROID_LOG_INFO, "SDL", "SDL2 Open file from android assets: %s\n", file);
    // Android SDL2 加载assets下的文件
    if (Android_JNI_FileOpen(rwops, file, mode) < 0) {
        SDL_FreeRW(rwops);
        return NULL;
    }
    rwops->size = Android_JNI_FileSize;
    rwops->seek = Android_JNI_FileSeek;
    rwops->read = Android_JNI_FileRead;
    rwops->write = Android_JNI_FileWrite;
    rwops->close = Android_JNI_FileClose;
    rwops->type = SDL_RWOPS_JNIFILE;

```

