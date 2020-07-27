package com.vimers.smartblock;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    public static Context contextOfApplication;
    private GifDrawable gifDrawable;
    private GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setOnTouchListener(this);
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.lock_simple);
        }
        catch (Exception e) {}
        gifImageView = (GifImageView) findViewById(R.id.gif);
        gifImageView.setImageDrawable(gifDrawable);
        contextOfApplication = getApplicationContext();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().setEnabled(true);
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.lock_simple);
        }
        catch (Exception e) {}
        gifDrawable.stop();
        gifImageView.setImageDrawable(gifDrawable);

    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }//Returns context to outer classes if needed

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        getWindow().getDecorView().setEnabled(false);
        gifDrawable.start();
        gifImageView.setImageDrawable(gifDrawable);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                gifDrawable.stop();
                gifImageView.setImageDrawable(gifDrawable);
                if(getSharedPreferences("APP_STATE", MODE_PRIVATE).getBoolean("REGISTRATION_COMPLETED", false)) {
                    startActivity(new Intent("com.vimers.smartblock.activity_input_password").putExtra("INTENT", "com.vimers.smartblock.activity_settings"));
                    try{
                        if(DialogDisplayService.isRunning()) {
                            stopService(new Intent(MainActivity.this ,DialogDisplayService.class));
                        }
                        else {
                            startService(new Intent(MainActivity.this ,DialogDisplayService.class));
                        }
                    }
                    catch (Exception e) {}
                }
                else {
                    startActivity(new Intent("com.vimers.smartblock.activity_registration"));
                }

                cancel();
            }
        }, gifDrawable.getDuration() - 100, gifDrawable.getDuration() - 100);
        return true;
    }
}
