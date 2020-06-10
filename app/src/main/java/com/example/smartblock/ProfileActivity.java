package com.example.smartblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



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




        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        final EditText childNameEditLine = findViewById(R.id.changeChildName);
        final EditText passwordEditLine = findViewById(R.id.changePassword);
        ImageButton imageButton = findViewById(R.id.changeSettingsButton);


        childNameEditLine.setHint(sharedPreferences.getString("CHILD_NAME", "Введите имя"));
        passwordEditLine.setHint(sharedPreferences.getString("PASSWORD", "Введите пароль"));



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sharedPreferences.edit();

                if(!childNameEditLine.getText().toString().isEmpty()) {
                    edit.putString("CHILD_NAME", childNameEditLine.getText().toString());
                    childNameEditLine.setHint(childNameEditLine.getText().toString());
                    childNameEditLine.setText("");
                }

                if(!passwordEditLine.getText().toString().isEmpty()) {
                    edit.putString("PASSWORD", passwordEditLine.getText().toString());
                    passwordEditLine.setHint(passwordEditLine.getText().toString());
                    passwordEditLine.setText("");
                }

                childNameEditLine.clearFocus();
                passwordEditLine.clearFocus();
                edit.apply();
            }
        });
    }
}
