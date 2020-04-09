# termux-sdl

这是一个termux sdl插件，为编译运行SDL2和native app程序！

同时我把ffplay也添加了进来，因此它也可以作为一个本地播放器，ffplay目前实现了进度条，时间的显示，滑动屏幕左边控制亮度，右边控制音量，进度条的实现是通过SDL2_gfx进行绘制的，时间的显示是通过SDL2_ttf来绘制的

ffplay目前存在的bug，当以倍速进行播放时，拖动进度条，时间不准确，视频与音频不同步

###### ffplay播放：
```bash
# example/SDL2/ffplay/ffplay 是一个shell命令，并不是真正的二进制文件
cp example/SDL2/ffplay/ffplay /data/data/com.termux/files/usr/bin

# 播放视频
ffplay -i /sdcard/video/test.mp4

# 2倍速度播放
ffplay -af atempo=2.0 -vf setpts=1/2*PTS -i /sdcard/video/test.mp4

# 任意倍速度播放：atempo=x setpts=1/x*PTS
......

```


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

This is a termux sdl plugin for compiling and running SDL2 and native app programs!

 At the same time, I added ffplay, so it can also be used as a local player. ffplay currently implements a progress bar, time display, slide the screen to control the brightness on the left, and the right to control the volume. The progress bar is drawn by SDL2_gfx  , The time display is drawn by SDL2_ttf

 The current bug of ffplay, when playing at playback speed, drag the progress bar, the time is inaccurate, and the video and audio are not synchronized

###### ffplay playing:
```bash
# example/SDL2/ffplay/ffplay is a shell command, not a real binary file
cp example/SDL2/ffplay/ffplay /data/data/com.termux/files/usr/bin

# Play video
ffplay -i /sdcard/video/test.mp4

# 2x speed playback
ffplay -af atempo=2.0 -vf setpts=1/2*PTS -i /sdcard/video/test.mp4

# play at any speed: atempo=x setpts=1/x*PTS
```



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

##### app [download](https://github.com/Lzhiyong/termux-sdl/releases/download/termux-sdl/app-debug_sign.apk)


 **** 

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


screenshot6.jpg
![image](https://raw.githubusercontent.com/Lzhiyong/termux-sdl/master/screenshots/screenshot6.jpg)
 **** 

