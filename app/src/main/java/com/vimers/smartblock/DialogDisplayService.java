package com.vimers.smartblock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;

public class DialogDisplayService extends Service {
    private String CHANNEL_ID = "DialogDisplayService";
    public static Timer alertDialogAppearanceTimer;
    public static int timePeriod = 15000;
    public static boolean isActive = false;
    private static Context context;

    //Outer apps don`t have access to to this service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //Basic OnCreate
    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        isActive = true;
    }
    //Creates new notification channel and sets notification
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = this;
        isActive = true;
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SmartBlock")
                .setContentText("Приложение запущено")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        alertDialogAppearanceTimer = new Timer();
        alertDialogAppearanceTimer.schedule(DialogTimer.getTimerTask(), timePeriod, timePeriod);

        return super.onStartCommand(intent, flags, startId);
    }
    //Stops service and timer
    @Override
    public void onDestroy() {
        super.onDestroy();
        isActive = false;
        alertDialogAppearanceTimer.cancel();
    }

    //Creates Notification channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    //Resets timer (new loop of displaying)
    public static void resetTimer() {
        alertDialogAppearanceTimer.cancel();
        alertDialogAppearanceTimer = new Timer();
        alertDialogAppearanceTimer.schedule(DialogTimer.getTimerTask(), timePeriod, timePeriod);
    }

    public static Context getContext(){
        return context;
    }
}

