package com.vimers.smartblock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.Timer;

public class DialogDisplayService extends Service {
    private static final String CHANNEL_ID = "DialogDisplayService";
    private static boolean running;
    private Timer alertDialogTimer;
    public static int timePeriod = 15000;
    private BlockedAppsSet blockedAppsSet;
    private final Binder binder = new Binder();

    public static boolean isRunning() {
        return running;
    }

    @SuppressWarnings("ReturnOfInnerClass")
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //Basic OnCreate
    @Override
    public void onCreate() {
        super.onCreate();
        running = true;
        blockedAppsSet = new BlockedAppsSet(getApplicationContext());
        createNotificationChannel();
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

    //Creates new notification channel and sets notification
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        displayNotification();

        alertDialogTimer = new Timer();
        BlockTimerTask blockTimerTask =
                new BlockTimerTask(getApplicationContext(), blockedAppsSet);
        alertDialogTimer.schedule(blockTimerTask, timePeriod, timePeriod);

        return super.onStartCommand(intent, flags, startId);
    }

    private void displayNotification() {
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
    }

    //Resets timer (new loop of displaying)
    public void resetTimer() {
        alertDialogTimer.cancel();
        alertDialogTimer = new Timer();
        BlockTimerTask blockTimerTask =
                new BlockTimerTask(getApplicationContext(), blockedAppsSet);
        alertDialogTimer.schedule(blockTimerTask, timePeriod, timePeriod);
    }

    //Stops service and timer
    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        alertDialogTimer.cancel();
    }

    class Binder extends android.os.Binder {
        DialogDisplayService getService() {
            return DialogDisplayService.this;
        }
    }
}

