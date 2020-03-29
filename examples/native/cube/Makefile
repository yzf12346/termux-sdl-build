#
# Native Activity plasma example
# type run to execute plasma
# type clean to clean sources
#

TARGET = libcube.so

all: $(TARGET)

CFLAGS = -Os -Wall -fPIC

OBJS = cube.o android_native_app_glue.o

LDFLAGS = -Wl,-soname,$(TARGET) -shared -Wl,--no-undefined -Wl,-z,noexecstack -llog -landroid -lm -lEGL -lGLESv1_CM

RUN = am start -S $(shell am 2>&1| grep -q '\-\-user' && echo '--user 0') -n com.termux.sdl/.TermuxNativeActivity -e nativeApp

$(TARGET): $(OBJS)
		$(CC) -o $@ $^ $(LDFLAGS)

clean:
		rm $(OBJS) $(TARGET)

run: $(TARGET)
		$(RUN) $(PWD)/$(TARGET)
