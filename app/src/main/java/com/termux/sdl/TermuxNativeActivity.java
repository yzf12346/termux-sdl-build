package com.termux.sdl;

import android.app.Activity;
import android.app.NativeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TermuxNativeActivity extends Activity {

    private static final String TAG = "TermuxNativeActivity";

    // the default native app 
    private String nativeApp = "libnative_loader.so";

    // error message
    private String errorMessage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        // nativeApp = your_project/libxxx.so
        nativeApp = getIntent().getStringExtra("nativeApp");

        // loading native lib to internal directory
        // to /data/user/0/com.termux.sdl/tmpdir
        if(copyLibFile()) {
            Intent intent = new Intent(this, NativeActivity.class);
            // from jni: native_loader.cpp
            intent.putExtra("nativeApp", nativeApp);
            // start native app
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_native);
            TextView textView = findViewById(R.id.nativeTextView);
            if(errorMessage == null)
                errorMessage = "Failed to load the nativeApp library, the parameters may be wrong.\n\n"
                               + "Make sure the command is correct.\n\n"
                               + "am start $(shell am 2>&1| grep -q '\\-\\-user' && echo '--user 0') -n com.termux.sdl/.TermuxNativeActivity -e nativeApp your_lib_pathname";
            textView.setText(errorMessage);
        }
    }


    public boolean copyLibFile() {
        if(nativeApp == null || nativeApp.isEmpty()) return false;
        nativeApp = nativeApp.trim();
        if((new File(nativeApp)).exists()) {
            String libDir = getCacheDir().getParentFile().getAbsolutePath() + "/tmpdir";
            String libFile = libDir + "/" + (new File(nativeApp)).getName();

            if(!(new File(libDir)).exists()) {
                (new File(libDir)).mkdir();
            }

            try {
                FileUtils.copyFile(new File(nativeApp), new File(libFile));
                Runtime.getRuntime().exec("chmod 755 " + libFile).waitFor();

                // Environment variables must be set, otherwise the program will not run correctly
                String pwd = new File(nativeApp).getParentFile().getAbsolutePath();
                Log.i(TAG, "chdir: " + pwd);
                JNI.chDir(pwd);
                JNI.setEnv("PWD", pwd, true);
                // nativeApp = /data/user/0/com.termux.sdl/tmpdir/libxxx.so
                nativeApp = libFile;

                FileOutputStream conf = new FileOutputStream(libDir + "/native_loader.conf");

                conf.write(nativeApp.getBytes());
                conf.close();

            } catch(IOException ex) {
                Log.e(TAG, "copy file failed: " + ex.getMessage());
                return false;
            } catch(InterruptedException ex) {
                Log.e(TAG, "exec cmd failed: " + ex.getMessage());
                return false;
            }
        }
        return true;
    }


    public void deleteLibFile() {
        // delete /data/user/0/com.termux.sdl/tmpdir/libxxx.so
        if(nativeApp != null && !nativeApp.isEmpty()) {
            File file = new File(nativeApp);
            if(file.exists()) {
                FileUtils.deleteFile(file);
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        deleteLibFile();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteLibFile();
    }
}
