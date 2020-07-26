package com.vimers.smartblock;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.goRegistrationBut).setOnClickListener(
                v -> startActivity(new Intent(this, RegistrationActivity.class))
        );
    }
}
