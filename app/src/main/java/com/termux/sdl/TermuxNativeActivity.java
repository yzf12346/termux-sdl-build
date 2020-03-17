package com.termux.sdl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import java.io.File;
import android.content.Intent;
import android.app.NativeActivity;
import java.io.FileOutputStream;
import android.widget.TextView;

public class TermuxNativeActivity extends Activity {

    //private static final String TAG = "TermuxNativeActivity";

    // the native app library
    private String nativeApp = "libnative_loader.so";

    // error message
    private String errorMessage = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nativeApp = getIntent().getStringExtra("nativeApp");

        // load native lib to internal directory
        if(loadLibFile()) {
            Intent intent = new Intent(this, NativeActivity.class);
            //from jni: loader.cpp
            intent.putExtra("nativeApp", nativeApp);
            //now run binary file
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


    public boolean loadLibFile() {
        if (null == nativeApp || "".equals(nativeApp)) return false;
        
        if ((new File(nativeApp)).exists()) {
            String libDir = getCacheDir().getParentFile().getAbsolutePath() + "/tmpdir";
            String libFile = libDir + "/" + (new File(nativeApp)).getName();
            
            if (!(new File(libDir)).exists()) {
                (new File(libDir)).mkdir();
            }

            try {
                Util.copyFile(new File(nativeApp), new File(libFile));
                Runtime.getRuntime().exec("chmod 755 " + libFile).waitFor();
                nativeApp = libFile;

                FileOutputStream conf = new FileOutputStream(libDir + "/native_loader.conf");

                conf.write(nativeApp.getBytes());
                conf.close();

            } catch (Exception ex) {
                errorMessage = ex.getMessage();
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }


    public void deleteLibFile() {
        if (null != nativeApp && !"".equals(nativeApp)) {
            File file = new File(nativeApp);
            if (file.exists()) {
                file.delete();
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
