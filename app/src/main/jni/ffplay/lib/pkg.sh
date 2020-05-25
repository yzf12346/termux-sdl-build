#!/bin/bash

$HOME/android/android-ndk-r21/toolchains/llvm/prebuilt/linux-aarch64/bin/aarch64-linux-android23-clang -pie -fPIC -shared -Wl,--whole-archive *.a -Wl,--no-whole-archive -o libffmpeg.so
