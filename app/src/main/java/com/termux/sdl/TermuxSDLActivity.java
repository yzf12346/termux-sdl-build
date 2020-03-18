package com.termux.sdl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.File;
import org.libsdl.app.SDLActivity;

public class TermuxSDLActivity extends SDLActivity {

    private static final String TAG = "TermuxSDLActivity";

    // the self lib pathname
    private String sdlmain = "libmain.so";

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] permission = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        for (String perm:permission) {
            if (!hasPermission(perm)) {
                applyPermission(perm);
            }
        }

        // sdlmain = your_project/libxxx.so
        sdlmain = getIntent().getStringExtra("sdlmain");
        // loading SDL lib to internal directory
        // to /data/user/0/com.termux.sdl/tmpdir/libxxx.so
        loadLibFile(); 

    }


    @Override
    protected String[] getArguments() {
        String args = getIntent().getStringExtra("args");
        if (null != args && !"".equals(args)) {
            //Log.i(TAG, args);
            return new String[]{args};
        } else {
            return super.getArguments();
        }
    }

  
    @Override
    protected String getMainSharedObject() {
        if (null != sdlmain && !"".equals(sdlmain)) {
            //Log.i(TAG, pathname);
            return sdlmain;
        } else {
            return super.getMainSharedObject();
        }
    }


    public void loadLibFile() {
        if (null == sdlmain || "".equals(sdlmain)) return ;
        
        if ((new File(sdlmain)).exists()) {
            String libDir = getCacheDir().getParentFile().getAbsolutePath() + "/tmpdir";
            String libFile = libDir + "/" + (new File(sdlmain)).getName();
            
            if (!(new File(libDir)).exists()) {
                (new File(libDir)).mkdir();
            }

            try {
                Util.copyFile(new File(sdlmain), new File(libFile));
                Runtime.getRuntime().exec("chmod 755 " + libFile).waitFor();
                
                // Environment variables must be set, otherwise the program will not run correctly
                String pwd = new File(sdlmain).getParentFile().getAbsolutePath();
                JNI.chDir(pwd);
                JNI.setEnv("PWD", pwd, true);
                // sdlmain = /data/user/0/com.termux.sdl/tmpdir/libxxx.so
                sdlmain = libFile;
            } catch (Exception ex) {
                Log.e(TAG, "copy sdlmain failed " + ex);
                showErrorDialog(ex.getMessage());
            }
        }
    }

  

//    private void restartActivity() {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                SDLActivity.nativeSendQuit();
//                Intent intent = getIntent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK 
//                                                            | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                overridePendingTransition(0, 0);
//                finish();
//
//                overridePendingTransition(0, 0);
//                startActivity(intent);
//            }
//        });
//    }
    
    
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

    public void deleteLibFile() {
        // delete /data/user/0/com.termux.sdl/tmpdir/libxxx.so
        if (null != sdlmain && !"".equals(sdlmain)) {
            File file = new File(sdlmain);
            if (file.exists()) {
                file.delete();
            }
        }
    }


    public boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        else
            return true;
    }

    public void applyPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(permission)) {
                Toast.makeText(this, "request read sdcard permmission", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{permission}, 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, TermuxNativeActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
