package com.termux.sdl;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import org.libsdl.app.SDLActivity;
import android.content.pm.PackageInfo;
import java.io.IOException;

public class TermuxSDLActivity extends SDLActivity {

    private static final String TAG = "TermuxSDLActivity";

    // the self lib pathname
    private String sdlmain = "libmain.so";

    // get the sdl2 libraries version
    private enum SDLVersion {
        SDL2, SDL2_image, SDL2_mixer, SDL2_net, SDL2_ttf;
    }

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
        Log.i(TAG, "sdlmain: " + sdlmain);
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
                Log.i(TAG, "chdir to: " + pwd);
                JNI.chDir(pwd);
                JNI.setEnv("PWD", pwd, true);
                
                // sdlmain = /data/user/0/com.termux.sdl/tmpdir/libxxx.so
                sdlmain = libFile;
            } catch (IOException ex) {
                Log.e(TAG, "copy file failed: " + ex.getMessage());
                showErrorDialog(ex.getMessage());
            } catch (InterruptedException ex) {
                Log.e(TAG, "exec cmd failed: " + ex.getMessage());
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
            Log.i(TAG, "delete sdlmain: " + file.getAbsolutePath());
            if (file.exists()) {
                Util.deleteFile(file);
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
            case R.id.action_about:
                aboutDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void aboutDialog() {
        String versionName = null;    
        try {
            PackageManager pkg = getPackageManager();
            PackageInfo info = pkg.getPackageInfo(getPackageName(), 0);
            versionName = pkg.getApplicationLabel(info.applicationInfo) + ": " +info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = e.getMessage();
        }
        final TextView textView = new TextView(this);
        textView.setAutoLinkMask(Linkify.WEB_URLS);
        textView.setLinksClickable(true);
        textView.setTextSize(18f);
        textView.setPadding(60, 60, 0, 0);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        textView.setText(versionName + "\n"
                       + getString(R.string.sdl_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2.ordinal()) + "\n" 
                       + getString(R.string.sdl_image_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2_image.ordinal()) + "\n" 
                       + getString(R.string.sdl_mixer_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2_mixer.ordinal()) + "\n" 
                       + getString(R.string.sdl_net_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2_net.ordinal()) + "\n" 
                       + getString(R.string.sdl_ttf_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2_ttf.ordinal()) + "\n"
                       );

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        new AlertDialog.Builder(this)
            .setTitle(getString(R.string.about_dialog))
            .setView(textView)
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            }).show();
    }
}
