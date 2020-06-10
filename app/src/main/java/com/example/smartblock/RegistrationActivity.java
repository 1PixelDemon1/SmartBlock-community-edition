package com.example.smartblock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    SharedPreferences sp;
    private TextView childNameField, passwordField;
    private ImageButton continueButton;

    private void checkTextFields() {
        boolean canContinue = !childNameField.getText().toString().isEmpty() && !passwordField.getText().toString().isEmpty();
        continueButton.setEnabled(canContinue);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        // Получим ссылку на лист 1
        ImageView listImageView_one = (ImageView) findViewById(R.id.imageView4);
        // Анимация для появления листа 1
        Animation ListRiseAnimation_one = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_one_2);
        // Подключаем анимацию к нужному View
        listImageView_one.startAnimation(ListRiseAnimation_one);

        // Получим ссылку на лист 2
        ImageView listImageView_two = (ImageView) findViewById(R.id.imageView6);
        // Анимация для появления листа 2
        Animation ListRiseAnimation_two = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_two_2);
        // Подключаем анимацию к нужному View
        listImageView_two.startAnimation(ListRiseAnimation_two);



        sp = getSharedPreferences("settings", MODE_PRIVATE);
        childNameField = findViewById(R.id.getChildNameLine);
        passwordField = findViewById(R.id.getPasswordLine);
        continueButton = findViewById(R.id.goSettingsBut);


        Animation ListRiseAnimation_getChildNameLine = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_textviev);
        childNameField.startAnimation(ListRiseAnimation_getChildNameLine);


        Animation ListRiseAnimation_getPasswordLine = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_textviev_one);
        passwordField.startAnimation(ListRiseAnimation_getPasswordLine);


        continueButton.setEnabled(false);
        TextWatcher fieldChangeListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkTextFields();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkTextFields();
            }
        };


        childNameField.addTextChangedListener(fieldChangeListener);
        passwordField.addTextChangedListener(fieldChangeListener);
        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("CHILD_NAME", childNameField.getText().toString());
                ed.putString("PASSWORD", passwordField.getText().toString());
                ed.apply();
                Intent intent = new Intent("com.example.smartblock.activity_settings");
                startActivity(intent);
            }
        });
    }
}
