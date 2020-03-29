#
# Native Activity example
# type run to execute Native Activity
# type clean to clean sources
#

TARGET = libnative-activity.so

all: $(TARGET)

TERMUX_PREFIX = /data/data/com.termux/files/usr

CFLAGS = -Os -Wall -I.

OBJS = native-activity.o android_native_app_glue.o

LDFLAGS = -Wl,-soname,$(TARGET) -shared -Wl,--no-undefined -Wl,-z,noexecstack -llog -landroid -lEGL -lGLESv1_CM


RUN = am start -S $(shell am 2>&1| grep -q '\-\-user' && echo '--user 0') -n com.termux.sdl/.TermuxNativeActivity -e nativeApp


$(TARGET): $(OBJS)
		$(CC) -o $@ $^ $(LDFLAGS)

clean:
		rm $(OBJS) $(TARGET)

run: $(TARGET)
		$(RUN) $(PWD)/$(TARGET)
