package com.example.smartblock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class AppLaunchDetectionService extends Service {
    private SharedPreferences currentSavedState;
    private SharedPreferences sp;
    private SharedPreferences ba;
    private SharedPreferences curPassState;



    private Timer timer;
    private Timer debugTimer;
    private Timer stopperTimer;

    private AlertDialog alertDialog;

    //useless for now
    //private Intent intent;
    //private String intentName = "com.example.smartblock.activity_math_blocker";

    private Random random = new Random();
    private int timeDelta;
    private int maxNum;
    //useless for now

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        timer = new Timer();
        debugTimer = new Timer();
        stopperTimer = new Timer();

        currentSavedState = getSharedPreferences("state", MODE_PRIVATE);
        sp = getSharedPreferences("settings", MODE_PRIVATE);
        ba = getSharedPreferences("block_apps", MODE_PRIVATE);
        curPassState = getSharedPreferences("current_password_state", MODE_PRIVATE);


        maxNum = sp.getInt("BORDER_VALUE", 0) + 1;
        timeDelta = sp.getInt("TIME_VALUE", 1);
        SharedPreferences.Editor e = currentSavedState.edit();
        e.putString("currentActivityState", "off");
        e.apply();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else
            buildNotification();

        start();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }


    private void start() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        checkForRunningApps();
                    }
                });
            }
        }, 0, timeDelta * 60000);

        stopperTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        if (checkForOpenedSettings() && curPassState.getBoolean("TIME_LEFT", true)) {//если запущено окно настроек, то
                            SharedPreferences.Editor editor = curPassState.edit();
                            editor.putBoolean("PASSWORD_OPEN", true); //устанавливаем, что пользователь не запускал окно
                            editor.apply();

                            startActivity(new Intent("com.example.smartblock.activity_password").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                });
            }
        }, 0, 500);


//        debugTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                printState();
//            }
//        }, 0, 1000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        debugTimer.cancel();

        stopForeground(true);

        SharedPreferences.Editor e = currentSavedState.edit();
        e.putString("currentActivityState", "off");
        e.apply();
        alertDialog.dismiss();

        curPassState = getSharedPreferences("current_password_state", MODE_PRIVATE);
        SharedPreferences.Editor editor = curPassState.edit();
        editor.clear();
        editor.apply();
    }

    private void printState() {
        Log.d("Printer", currentSavedState.getString("currentActivityState", ""));
    }

    private boolean checkForOpenedSettings() {
        return printForegroundTask().equals("com.android.settings");
    }

    public void start_my_timer() {
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        checkForRunningApps();
                    }
                });
            }
        }, 0, timeDelta);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkForRunningApps() {
        ba = getSharedPreferences("block_apps", MODE_PRIVATE);
        Set<String> ret = ba.getStringSet("blocked_apps", new HashSet<String>());

        //Toast.makeText(getApplicationContext(), printForegroundTask(), Toast.LENGTH_LONG).show();
        String Text = printForegroundTask();
        currentSavedState = getSharedPreferences("state", MODE_PRIVATE);
        if (currentSavedState.getString("currentActivityState", "").equals("off") || currentSavedState.getString("currentActivityState", "").equals("")) {
            for (String text : ret) {
                if (text.equals(Text)) {
                    SharedPreferences.Editor e = currentSavedState.edit();
                    e.putString("currentActivityState", "on");
                    e.apply();
                    buildDialog();
                }
            }
        }
    }

    private String printForegroundTask() {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService("usagestats");
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }


        return currentApp;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void buildDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.dialog_blocker, null);
        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("Проверить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                .create();

        if (Build.VERSION.SDK_INT < 26) {
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        } else {
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //create a new one
        layoutParams.gravity = Gravity.CENTER; //this is layout_gravity

        final int num1 = random.nextInt(maxNum) + 1;
        final int num2 = random.nextInt(maxNum) + 1;

        sp = getSharedPreferences("settings", MODE_PRIVATE);

        int mas[] = new int[4];
        int counter = 0;
        if (sp.getBoolean("PLUS_MODE", false)) {
            mas[counter] = 0;
            counter++;
        }
        if (sp.getBoolean("MINUS_MODE", false)) {
            mas[counter] = 1;
            counter++;
        }
        if (sp.getBoolean("X_MODE", false)) {
            mas[counter] = 2;
            counter++;
        }
        if (sp.getBoolean("DEL_MODE", false)) {
            mas[counter] = 3;
            counter++;
        }

        int unm;
        String answer = "";

        if (counter == 0) unm = 0;
        else unm = mas[random.nextInt(counter)];


        switch (unm) {
            case 0:
                ((TextView) alertDialog.findViewById(R.id.exampleText)).setText(getStringNum(num1) + " + " + getStringNum(num2) + " =");
                answer = getStringNum(num1 + num2);
                break;
            case 1:
                ((TextView) alertDialog.findViewById(R.id.exampleText)).setText(getStringNum(num1 > num2 ? num1 : num2) + " - " + getStringNum(num1 > num2 ? num2 : num1) + " =");

                answer = getStringNum(num1 < num2 ? num2 - num1 : num1 - num2);
                break;

            case 2:
                ((TextView) alertDialog.findViewById(R.id.exampleText)).setText(getStringNum(num1) + " × " + getStringNum(num2) + " =");
                answer = getStringNum(num1 * num2);
                break;

            case 3:
                ((TextView) alertDialog.findViewById(R.id.exampleText)).setText(getStringNum(num1 * num2) + " ÷ " + getStringNum(num2) + " =");
                answer = getStringNum(num1);
                break;

        }

        final String str = answer;

        ((EditText) alertDialog.findViewById(R.id.answerEdit)).setText("");
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(24, 241, 255));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(layoutParams);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) alertDialog.findViewById(R.id.answerEdit)).getText().toString().equals(str)) {
                    alertDialog.dismiss();
                    SharedPreferences.Editor e = currentSavedState.edit();
                    e.putString("currentActivityState", "off");
                    e.apply();
                    pause();
                    resume();
                } else {
                    ((TextView) alertDialog.findViewById(R.id.wrongText)).setText(getSharedPreferences("settings", MODE_PRIVATE).getString("CHILD_NAME", "") + ", " + "неверно, попробуй еще раз");
                    ((EditText) alertDialog.findViewById(R.id.answerEdit)).setText("");
                }
            }
        });
    }


    //useless for now
//    private void buildIntent() {
//        intent = (new Intent(intentName));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//    }


    private void buildNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher);
        Notification notification;
        if (Build.VERSION.SDK_INT < 16)
            notification = builder.getNotification();
        else
            notification = builder.build();
        startForeground(777, notification);
//        Intent hideIntent = new Intent(this, HideNotificationService.class);
//        startService(hideIntent);
    }

    public void pause() {
        timer.cancel();
    }

    public void resume() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        checkForRunningApps();
                    }
                });
            }
        }, timeDelta * 60000, timeDelta * 60000);
    }

    private String getStringNum(int num) {
        return String.format("%d", num);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.smartblock";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(777, notification);
        Intent hideIntent = new Intent(this, HideNotificationService.class);
        startService(hideIntent);
    }
}