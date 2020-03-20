#
# SDL application Makefile
# type args to build
# type run to execute
# type clean to clean sources
# type log to check application log
#


TARGET = playmus

all: $(TARGET)

TERMUX_PREFIX = /data/data/com.termux/files/usr

CFLAGS = -Os -Wall -DANDROID -I${TERMUX_PREFIX}/include/SDL2

LDFLAGS = -shared -L${TERMUX_PREFIX}/lib -lSDL2 -lSDL2_mixer -llog -lm

SDLRUN = am start $(shell am 2>&1| grep -q '\-\-user' && echo '--user 0') -n com.termux.sdl/.TermuxSDLActivity -e sdlmain

# change /sdcard/Music/芒种-音阙诗听_赵方婧.flac to your song path
ARGS = #--es args "/sdcard/Music/芒种-音阙诗听_赵方婧.flac"


OBJS = $(TARGET).o

$(TARGET): $(OBJS)
	$(CC) -o lib$@.so $^ $(LDFLAGS)

clean:
		rm -f $(OBJS) lib$(TARGET).so

run: $(TARGET)
		$(SDLRUN) $(PWD)/lib$(TARGET).so $(ARGS)

log:
	logcat -d | grep SDL
