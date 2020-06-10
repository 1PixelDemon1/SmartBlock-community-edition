package com.example.smartblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class MathBlocker extends AppCompatActivity {

    private SharedPreferences currentSavedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentSavedState = getSharedPreferences("state", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_blocker);
        ((TextView)findViewById(R.id.Local)).setText(Integer.toString(new Random().nextInt(100)));
    }


    @Override
    protected void onPause() {

        if (currentSavedState.getString("currentActivityState", "").equals("on")) {
            SharedPreferences.Editor editor = currentSavedState.edit();
            editor.putString("currentActivityState", "pause");
            editor.apply();
        }

        super.onPause();
    }
    //обработка выхода в фон
/******************************************************************************************************************/
    @Override
    public void onResume(){
        SharedPreferences.Editor editor = currentSavedState.edit();
        editor.putString("currentActivityState", "on");
        editor.apply();
        super.onResume();
    }
}
