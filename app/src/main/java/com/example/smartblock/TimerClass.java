package com.example.smartblock;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TimerClass {
    static int timeLeft;


    public static boolean setTimer(final Timer timer, final int period, final SharedPreferences sharPref) {//отключаем автозапуск при открытых настройках на period
        timeLeft = 0;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeLeft++;
                if (timeLeft == period) {
                    SharedPreferences.Editor edit = sharPref.edit();
                    edit.putBoolean("TIME_LEFT", true);
                    edit.apply();
                    timer.cancel();
                }
            }
        }, 0,1000);
        return true;
    }


}
