package com.vimers.smartblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    public static Context contextOfApplication;

    private ImageButton goRegistrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();

        goRegistrationButton = findViewById(R.id.goRegistrationBut);

        goRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.vimers.smartblock.activity_registration"));
            }
        });

    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }//Returns context to outer classes if needed

}
