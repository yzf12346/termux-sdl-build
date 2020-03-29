#
# Native Activity example
# type run to execute Native Activity
# type clean to clean sources
#


# Don't rename libvulkan.so
# conflicts with the system's libvulkan.so
TARGET = libvulkan_wrapper.so

all: $(TARGET)

CXXFLAGS = -Os -fPIC -Wall 

OBJS = main.o vulkan_wrapper.o

LDFLAGS = -Wl,-soname,$(TARGET) -shared -Wl,--no-undefined -Wl,-z,noexecstack -llog -landroid -lEGL -lGLESv1_CM -lGLESv2


RUN = am start -S $(shell am 2>&1| grep -q '\-\-user' && echo '--user 0') -n com.termux.sdl/.TermuxNativeActivity -e nativeApp


$(TARGET): $(OBJS)
		$(CXX) $(CXXFLAGS) -o $@ $^ $(LDFLAGS)

clean:
		rm $(OBJS) $(TARGET)

run: $(TARGET)
		$(RUN) $(PWD)/$(TARGET)
