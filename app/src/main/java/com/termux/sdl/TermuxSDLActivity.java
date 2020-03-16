package com.termux.sdl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.File;
import org.libsdl.app.SDLActivity;

public class TermuxSDLActivity extends SDLActivity {

    private static final String TAG = "TermuxSDLActivity";

    // get the parameters passed by am command 
    private Bundle mBundle;

    // the self lib path name
    private String pathname = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] permission = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
        };

        for (String perm:permission) {
            if (!hasPermission(perm)) {
                applyPermission(perm);
            }
        }

        mBundle = getIntent().getExtras();
        if(mBundle != null) {
            loadLibFile(); // load self lib
        }
    }

    
    @Override
    protected String[] getArguments() {

        if(mBundle != null) {
            //Log.i(TAG, mBundle.getString("args"));
            String[] args = {mBundle.getString("args")};
            return args;
        } else {
            return super.getArguments();
        }
    }


    @Override
    protected String getMainSharedObject() {

        if (!pathname.equals("")) {
            //Log.i(TAG, pathname);
            return pathname;
        } else {
            return super.getMainSharedObject();
        }
    }


    public void loadLibFile() {

        String sdlmain = mBundle.getString("sdlmain");
        if (sdlmain == null || sdlmain.equals("")) return ;
        String libDir = getCacheDir().getParentFile().getAbsolutePath() + "/" + "tmpdir";
        String libFile = libDir + "/" + (new File(sdlmain)).getName();
        pathname = libFile;

        if ((new File(sdlmain)).exists()) {
            if (!(new File(libDir)).exists()) {
                (new File(libDir)).mkdir();
            }

            try {
                Util.copyFile(new File(sdlmain), new File(libFile));
                Runtime.getRuntime().exec("chmod 755 " + libFile).waitFor();
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                Log.e(TAG, "copy sdlmain failed " + ex);
                showErrorDialog(ex.getMessage());
            }
            try {
                //System.load(libFile);
                String pwd = new File(sdlmain).getParentFile().getAbsolutePath();
                //Log.i(TAG, "chdir to " + pwd);
                JNI.chDir(pwd);
                JNI.setEnv("PWD", pwd, true);
            } catch (UnsatisfiedLinkError e) {
                System.out.println("Error: " + e.getMessage());
                Log.e(TAG, "Native code library failed to load.\n" + e);
                showErrorDialog(e.getMessage());
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

    public void deleteLibFile() {
        File file = new File(pathname);
        if (file.exists()) {
            file.delete();
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
