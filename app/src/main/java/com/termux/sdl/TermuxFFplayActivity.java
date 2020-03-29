package com.termux.sdl;

import android.content.Intent;
import android.os.Bundle;
import org.libsdl.app.SDLActivity;


// ffplay argv

public class TermuxFFplayActivity extends SDLActivity {

    private String argv = "ffplay";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
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
        finish();
    }
}
