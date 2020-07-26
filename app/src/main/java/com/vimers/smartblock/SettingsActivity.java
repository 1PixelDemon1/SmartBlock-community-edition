package com.vimers.smartblock;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.goAppListButton)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, AppListActivity.class)));
        findViewById(R.id.completeSettingsButton)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, DurationSettingsActivity.class)));
        findViewById(R.id.goMathSettingsButton)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, MathSettingsActivity.class)));
        findViewById(R.id.goBackgroundWorkingButton)
                .setOnClickListener(v -> startBlockerService());
    }

    private void startBlockerService() {
        startService(new Intent(this, DialogDisplayService.class));
    }
}
