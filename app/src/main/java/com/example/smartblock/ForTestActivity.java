package com.example.smartblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class ForTestActivity extends AppCompatActivity {
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_test);
        sp = getSharedPreferences("settings", MODE_PRIVATE);


        Set<String> ret = sp.getStringSet("blocked_apps", new HashSet<String>());
        for (String text : ret) {
            ((TextView) findViewById(R.id.OutputMas)).setText(((TextView) findViewById(R.id.OutputMas)).getText() + "\n" + text);
        }
        ((TextView) findViewById(R.id.OutputMas)).setText(((TextView) findViewById(R.id.OutputMas)).getText() + "\n" + sp.getString("CHILD_NAME", ""));
        ((TextView) findViewById(R.id.OutputMas)).setText(((TextView) findViewById(R.id.OutputMas)).getText() + "\n" + sp.getString("PASSWORD", ""));
    }
}
