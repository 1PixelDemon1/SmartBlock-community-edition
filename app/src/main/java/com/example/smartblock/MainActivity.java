package com.example.smartblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import co.nedim.maildroidx.MaildroidX;
import co.nedim.maildroidx.MaildroidXType;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences SharPref;
    private SharedPreferences SettPreferences;

    ImageButton AppCompatActivityImageButton;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            SharPref = getSharedPreferences("settings", MODE_PRIVATE);
            SettPreferences = getSharedPreferences("current_password_state", MODE_PRIVATE);


            ImageButton AppCompatActivityImageButton = findViewById(R.id.imageButton1);


            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
            //      Ярик, это просто кнопка, ты заменищь ее на ImageButton и добавишь анимацию  //
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

            ImageButton goProfileEditBtn = findViewById(R.id.goProfileButton);


            if(SharPref.getString("CURRENT_APP_STATE", "").equals("ON")){
                AppCompatActivityImageButton.setImageResource(R.drawable.go_settings);
                goProfileEditBtn.setVisibility(View.VISIBLE);
                goProfileEditBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = SharPref.edit();
                        editor.putString("intent", "profile");
                        editor.apply();
                        SharedPreferences.Editor edit = SettPreferences.edit();
                        edit.putBoolean("PASSWORD_OPEN", false);//установка, что пользователь открыл приложение сам
                        edit.apply();
                        startActivity(new Intent("com.example.smartblock.activity_password").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME));
                    }
                });
            }

            // Получим ссылку на лист 1
            ImageView listImageView_one = (ImageView) findViewById(R.id.imageView4);
            // Анимация для появления листа 1
            Animation ListRiseAnimation_one = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_one_1);
            // Подключаем анимацию к нужному View
            listImageView_one.startAnimation(ListRiseAnimation_one);

            // Получим ссылку на лист 2
            ImageView listImageView_two = (ImageView) findViewById(R.id.imageView6);
            // Анимация для появления листа 2
            Animation ListRiseAnimation_two = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_two_1);
            // Подключаем анимацию к нужному View
            listImageView_two.startAnimation(ListRiseAnimation_two);


            ImageView listImageView1 = (ImageView) findViewById(R.id.imageView1);
            Animation ListRiseAnimation_imageView1 = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_imge_one);
            listImageView1.startAnimation(ListRiseAnimation_imageView1);


            ImageView listImageView3 = (ImageView) findViewById(R.id.imageView3);
            Animation ListRiseAnimation_imageView3 = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_imge_one);
            listImageView3.startAnimation(ListRiseAnimation_imageView3);






            AppCompatActivityImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    new MaildroidX.Builder()
//                            .smtp("smtp.gmail.com")
//                            .smtpUsername("jj4971221@gmail.com")
//                            .smtpPassword("Nikita!1290")
//                            .smtpAuthentication(true)
//                            .port("465")
//                            .type(MaildroidXType.HTML)
//                            .to("pxldem@gmail.com")
//                            .from("SmartBlock")
//                            .subject("Title")
//                            .body("Body")
//                            .onCompleteCallback(new MaildroidX.onCompleteCallback() {
//                                @Override
//                                public void onSuccess() {
//                                    Log.d("MaildroidX",  "SUCCESS");
//                                }
//
//                                @Override
//                                public void onFail(String s) {
//                                    Log.d("MaildroidX",  "FAIL");
//                                }
//
//                                @Override
//                                public long getTimeout() {
//                                    return 3000;
//                                }
//                            })
//	                        .mail();

                    if(!SharPref.getString("CURRENT_APP_STATE", "").equals("ON")) {
                        startActivity(new Intent("com.example.smartblock.activity_registration"));
                    } else {
                        SharedPreferences.Editor editor = SharPref.edit();
                        editor.putString("intent", "settings");
                        editor.apply();
                        SharedPreferences.Editor edit = SettPreferences.edit();//запуск во второй(3,4,5...) раз
                        edit.putBoolean("PASSWORD_OPEN", false);//установка, что пользователь открыл приложение сам
                        edit.apply();
                        startActivity(new Intent("com.example.smartblock.activity_password").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME));
                    }


                }
            });
            }
        }







