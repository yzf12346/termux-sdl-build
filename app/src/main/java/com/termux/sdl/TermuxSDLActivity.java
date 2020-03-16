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
    
    private String pathname = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String[] permission = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
        };
        
     
        for(String perm:permission){
            if(!hasPermission(perm)){
                applyPermission(perm);
            }
        }
        
        loadLibFile(); // load self lib
    }

    
    @Override
    protected String getMainSharedObject() {
       
        if(!pathname.equals(""))
            return pathname;
        else
            return super.getMainSharedObject();
    }

  
    public void loadLibFile() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String sdlmain = bundle.getString("sdlmain");
            if(sdlmain == null || sdlmain.equals("")) return ;
            String libDir = getCacheDir().getParentFile().getAbsolutePath() + "/" + "tmpdir";
            String libFile = libDir + "/" + (new File(sdlmain)).getName();
            pathname = libFile;

            if ((new File(sdlmain)).exists()) {
                if (!(new File(libDir)).exists()) {
                    (new File(libDir)).mkdir();
                }

                try {
                    Util.copyDirectory(new File(sdlmain), new File(libFile));
                    Runtime.getRuntime().exec("chmod 755 " + libFile).waitFor();
                } catch (Exception ex) {
                    Log.e(TAG, "copy sdlmain failed " + ex);
                    showErrorDialog(ex.getMessage());
                }
                try {
                    System.load(libFile);
                    //(new File(libFile)).delete();
                    String pwd = new File(sdlmain).getParentFile().getAbsolutePath();
                    Log.i(TAG, "chdir to " + pwd);
                    JNI.chDir(pwd);
                    JNI.setEnv("PWD", pwd, true);
                } catch (UnsatisfiedLinkError e) {
                    Log.e(TAG, "Native code library failed to load.\n" + e);
                    showErrorDialog(e.getMessage());
                }
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
        if(file.exists()) {
            file.delete();
        }
    }
    
    
    public boolean hasPermission(String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        else
            return true;
    }

    public void applyPermission(String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(shouldShowRequestPermissionRationale(permission)){
                Toast.makeText(this, "request read sdcard permmission", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{permission},0);
        }
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch(item.getItemId()){
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
