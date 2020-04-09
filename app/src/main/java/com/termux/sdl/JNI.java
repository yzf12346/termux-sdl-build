package com.termux.sdl;

public final class JNI {

    static {
        System.loadLibrary("termux_sdl");
    }

    public static native int setEnv(String name, String value, boolean overwrite);

    public static native int unSetEnv(String name);

    public static native String getEnv(String name);

    public static native int chDir(String path);

    public static native String getSDLVersion(int lib);

    public static native String getFFmpegVersion();

}
