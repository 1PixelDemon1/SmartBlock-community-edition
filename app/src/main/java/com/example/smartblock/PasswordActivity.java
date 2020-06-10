package com.example.smartblock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Timer;

public class PasswordActivity extends AppCompatActivity {
    private SharedPreferences SharPref;
    private SharedPreferences SettPreferences;
    private int deltaSettingsPaused = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        final EditText passwordBox = findViewById(R.id.CheckPasswordInput);
        final TextView mistakeBoxOut = findViewById(R.id.mistakeOut);


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


        TextView listCheckPasswordInput = (TextView) findViewById(R.id.CheckPasswordInput);
        Animation ListRiseAnimation_CheckPasswordInput = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_text);
        listCheckPasswordInput.startAnimation(ListRiseAnimation_CheckPasswordInput);




        SharPref = getSharedPreferences("settings", MODE_PRIVATE);
        SettPreferences = getSharedPreferences("current_password_state", MODE_PRIVATE);



        findViewById(R.id.GoCheckPasswordBut).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (passwordBox.getText().toString().equals(SharPref.getString("PASSWORD", ""))) {
                    if (SettPreferences.getBoolean("PASSWORD_OPEN", false)) { //если поступила информация, что ребенок открыл настройки, то
                        SharedPreferences.Editor ed = SettPreferences.edit();
                        ed.putBoolean("TIME_LEFT", false);//устанавливаем паузу в автозакрытии настроек
                        ed.apply();

                        TimerClass.setTimer(new Timer(), deltaSettingsPaused, SettPreferences);//на определенное "deltaSettingsPaused" время
                        finishAffinity();//закрываем активность со страницей пароля
                    } else {
                        mistakeBoxOut.setText("");
                        passwordBox.setText("");
                        switch(SharPref.getString("intent", "")){ //в переменной SharedPreferences SharPref с именем settings хранится поле с данными о намерении пользователя
                            case "settings":
                                startActivity(new Intent("com.example.smartblock.activity_settings")); //запускаем настройки
                                break;
                            case "profile":
                                startActivity(new Intent("com.example.smartblock.activity_profile")); //запускаем настройки
                                break;
                            }
                        }
                } else {
                    mistakeBoxOut.setText("Неверный пароль");
                    passwordBox.setText("");
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(SettPreferences.getBoolean("PASSWORD_OPEN", false)){
            //nothing here
        }else{
            super.onBackPressed();
        }
    }
}
