package com.vimers.smartblock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();

        findViewById(R.id.goRegistrationBut).setOnClickListener(
                v -> startActivity(new Intent("com.vimers.smartblock.activity_registration"))
        );
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }//Returns context to outer classes if needed
}
