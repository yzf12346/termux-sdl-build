package com.termux.sdl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import org.libsdl.app.SDLActivity;


// using ffplay to playing video and audio 

public class TermuxFFplayActivity extends SDLActivity {

    private final static String TAG = "TermuxFFplayActivity";
    
    // the ffplay command params
    private String argv = "ffplay";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        // get the arguments from intent
        argv = getIntent().getStringExtra("argv");
    }


    @Override
    protected String getMainSharedObject() {
        if(argv != null && !argv.isEmpty())
            return "libffplay.so";
        else
            return super.getMainSharedObject();
    }

    
    @Override
    protected String[] getArguments() {
        if(argv != null && !argv.isEmpty())
            return argv.trim().split(" ");
        else
            return super.getArguments();
    }
    
    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
