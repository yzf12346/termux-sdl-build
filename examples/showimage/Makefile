#
# SDL application Makefile
# type args to build
# type run to execute
# type clean to clean sources
# type log to check application log
#


TARGET = showimage

all: $(TARGET)

TERMUX_PREFIX = /data/data/com.termux/files/usr

CFLAGS = -Os -Wall -DANDROID -I${TERMUX_PREFIX}/include/SDL2

LDFLAGS = -shared -L${TERMUX_PREFIX}/lib -lSDL2 -lSDL2_image -llog -lm

SDLRUN = am start $(shell am 2>&1| grep -q '\-\-user' && echo '--user 0') -n com.termux.sdl/.TermuxSDLActivity -e sdlmain

OBJS = $(TARGET).o

$(TARGET): $(OBJS)
	$(CC) -o lib$@.so $^ $(LDFLAGS)

clean:
		rm -f $(OBJS) lib$(TARGET).so

# change /sdcard/SDL2/showimage/picture.jpg to your image path 
run: $(TARGET)
		$(SDLRUN) $(PWD)/lib$(TARGET).so --es args "/sdcard/SDL2/showimage/picture.jpg"

log:
	logcat -d | grep SDL
