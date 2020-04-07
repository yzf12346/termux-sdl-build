package com.termux.sdl;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import org.libsdl.app.SDLActivity;

public class TermuxSDLActivity extends SDLActivity {

    private static final String TAG = "TermuxSDLActivity";

    private SharedPreferences mPrefer;
    private Button positiveButton;

    // the self lib pathname
    private String sdlmain = "libmain.so";

    // get the sdl2 libraries version
    private enum SDLVersion {
        SDL2, SDL2_image, SDL2_mixer, SDL2_net, SDL2_ttf, SDL2_gfx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefer = PreferenceManager.getDefaultSharedPreferences(this);

        String[] permission = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        for(String perm : permission) {
            if(!hasPermission(perm)) {
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
        if(args != null && !args.isEmpty()) {
            //Log.i(TAG, args);

            return args.trim().split(" ");
        } else {
            return super.getArguments();
        }
    }


    @Override
    protected String getMainSharedObject() {
        if(sdlmain != null && !sdlmain.isEmpty()) {
            //Log.i(TAG, pathname);
            return sdlmain;
        } else {
            return super.getMainSharedObject();
        }
    }


    public void loadLibFile() {
        if(sdlmain == null || sdlmain.isEmpty()) return ;
        sdlmain = sdlmain.trim();
        if((new File(sdlmain)).exists()) {
            String libDir = getCacheDir().getParentFile().getAbsolutePath() + "/tmpdir";
            String libFile = libDir + "/" + (new File(sdlmain)).getName();

            if(!(new File(libDir)).exists()) {
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
            } catch(IOException ex) {
                Log.e(TAG, "copy file failed: " + ex.getMessage());
                showErrorDialog(ex.getMessage());
            } catch(InterruptedException ex) {
                Log.e(TAG, "exec cmd failed: " + ex.getMessage());
                showErrorDialog(ex.getMessage());
            }
        }
    }


    // restart activity
//    private void restartActivity() {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                SDLActivity.nativeSendQuit();
//
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
//

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
        if(sdlmain != null && !sdlmain.isEmpty()) {
            File file = new File(sdlmain);
            Log.i(TAG, "delete sdlmain: " + file.getAbsolutePath());
            if(file.exists()) {
                //Util.deleteFile(file);
            }
        }
    }


    public boolean hasPermission(String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        else
            return true;
    }

    public void applyPermission(String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(shouldShowRequestPermissionRationale(permission)) {
                Toast.makeText(this, "request read sdcard permmission", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[] {permission}, 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
        case R.id.action_settings:
            startActivity(new Intent(this, TermuxNativeActivity.class));
            break;
        case R.id.action_ffplay:
            ffplayDialog();
            break;
        case R.id.action_about:
            aboutDialog();
            break;
        }
        return super.onOptionsItemSelected(item);
    }


    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO: Implement this method
            if(positiveButton == null) return;
            String text = s.toString().trim();
            if(text.isEmpty())
                positiveButton.setEnabled(false);
            else
                positiveButton.setEnabled(true);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO: Implement this method
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO: Implement this method
        }
    };


    private void ffplayDialog() {
        View view = getLayoutInflater().inflate(R.layout.cmd_dialog, null);
        final EditText cmdText = view.findViewById(R.id.cmd_text);
        final String cmd = mPrefer.getString("cmd", "").trim();
        cmdText.addTextChangedListener(watcher);
        if(!cmd.isEmpty()) {
            cmdText.setText(cmd);
        } else {
            cmdText.setHint("Usage: ffplay [options] input_file");
        }

        cmdText.requestFocus();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
        .setTitle("ffplay")
        .setView(view)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SDLActivity.nativeSendQuit();
                //SDLActivity.nativeQuit();
                String text = cmdText.getText().toString().trim();
                mPrefer.edit().putString("cmd", text).commit();

                if(text.startsWith("ffplay")) {
                    text = text.substring(6, text.length()).trim();
                }
                Intent intent = new Intent(TermuxSDLActivity.this, TermuxFFplayActivity.class);
                intent.putExtra("argv", text);
                startActivity(intent);
                finish();
            }
        })
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
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if(cmdText.getText().toString().trim().isEmpty())
            positiveButton.setEnabled(false);
        else
            positiveButton.setEnabled(true);

    }



    private void aboutDialog() {

        String versionName = null;
        try {
            PackageManager pkg = getPackageManager();
            PackageInfo info = pkg.getPackageInfo(getPackageName(), 0);
            versionName = pkg.getApplicationLabel(info.applicationInfo) + ": " + info.versionName;
        } catch(PackageManager.NameNotFoundException e) {
            versionName = e.getMessage();
        }

        View view = getLayoutInflater().inflate(R.layout.about_dialog, null);
        final TextView textView = view.findViewById(R.id.about_text);

        textView.setText(versionName + "\nhttps://github.com/Lzhiyong/termux-sdl\n\n"
                         + getString(R.string.sdl_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2.ordinal()) + "\n"
                         + "https://www.libsdl.org\n\n"
                         + getString(R.string.sdl_image_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2_image.ordinal()) + "\n"
                         + "https://www.libsdl.org/projects/SDL_image\n\n"
                         + getString(R.string.sdl_mixer_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2_mixer.ordinal()) + "\n"
                         + "https://www.libsdl.org/projects/SDL_mixer\n\n"
                         + getString(R.string.sdl_net_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2_net.ordinal()) + "\n"
                         + "https://www.libsdl.org/projects/SDL_net\n\n"
                         + getString(R.string.sdl_ttf_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2_ttf.ordinal()) + "\n"
                         + "https://www.libsdl.org/projects/SDL_ttf\n\n"
                         + getString(R.string.sdl_gfx_version) + ": " + JNI.getSDLVersion(SDLVersion.SDL2_gfx.ordinal()) + "\n"
                         + "http://www.ferzkopp.net/wordpress/2016/01/02/sdl_gfx-sdl2_gfx\n\n"
                        );

        //textView.setMovementMethod(LinkMovementMethod.getInstance());
        new AlertDialog.Builder(this)
        .setTitle(getString(R.string.about_dialog))
        .setView(view)
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
