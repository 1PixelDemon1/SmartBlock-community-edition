package com.example.smartblock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences SharPref;
    private SharedPreferences ba;
    private int value;
    private int timeDelta;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, DeviceAdminReceiver.class);


        SharPref = getSharedPreferences("settings", MODE_PRIVATE);
        ba = getSharedPreferences("block_apps", MODE_PRIVATE);

        value = SharPref.getInt("BORDER_VALUE", 0);
        timeDelta = SharPref.getInt("TIME_VALUE", 1);





        // Получим ссылку на лист 1
        ImageView listImageView_one = (ImageView) findViewById(R.id.imageView4);
        // Анимация для появления листа 1
        Animation ListRiseAnimation_one = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_one_3);
        // Подключаем анимацию к нужному View
        listImageView_one.startAnimation(ListRiseAnimation_one);

        // Получим ссылку на лист 2
        ImageView listImageView_two = (ImageView) findViewById(R.id.imageView6);
        // Анимация для появления листа 2
        Animation ListRiseAnimation_two = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_two_3);
        // Подключаем анимацию к нужному View
        listImageView_two.startAnimation(ListRiseAnimation_two);


        //
        ImageView listImageView_fife = (ImageView) findViewById(R.id.imageView5);
        Animation ListRiseAnimation_fife = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_imge_fife);
        listImageView_fife.startAnimation(ListRiseAnimation_fife);

        //
        ImageButton listImage_Buttom = (ImageButton) findViewById(R.id.goAppListBtn);
        Animation ListRiseAnimation_imge_Buttom = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_imge_buttom);
        listImage_Buttom.startAnimation(ListRiseAnimation_imge_Buttom);

        //
        ImageView listImage_seven = (ImageView) findViewById(R.id.imageView7);
        Animation ListRiseAnimation_imge_seven = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_imge_seven);
        listImage_seven.startAnimation(ListRiseAnimation_imge_seven);


        SeekBar list_chosenBorderBar = (SeekBar) findViewById(R.id.chosenBorderBar);
        Animation ListRiseAnimation_chosenBorderBar = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_border_bar);
        list_chosenBorderBar.startAnimation(ListRiseAnimation_chosenBorderBar);


        TextView listText_chosenBorderOutput = (TextView) findViewById(R.id.chosenBorderOutput);
        Animation ListRiseAnimation_chosenBorderOutput = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_border_bar);
        listText_chosenBorderOutput.startAnimation(ListRiseAnimation_chosenBorderOutput);


        Switch listSwitch_plusSwitch = (Switch) findViewById(R.id.plusSwitch);
        Animation ListRiseAnimation_plusSwitch = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_switch);
        listSwitch_plusSwitch.startAnimation(ListRiseAnimation_plusSwitch);


        Switch listSwitch_minusSwitch = (Switch) findViewById(R.id.minusSwitch);
        Animation ListRiseAnimation_minusSwitch = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_switch);
        listSwitch_minusSwitch.startAnimation(ListRiseAnimation_minusSwitch);


        Switch listSwitch_xSwitch = (Switch) findViewById(R.id.xSwitch);
        Animation ListRiseAnimation_xSwitch = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_switch);
        listSwitch_xSwitch.startAnimation(ListRiseAnimation_xSwitch);


        Switch listSwitch_delSwitch = (Switch) findViewById(R.id.delSwitch);
        Animation ListRiseAnimation_delSwitch = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_switch);
        listSwitch_delSwitch.startAnimation(ListRiseAnimation_delSwitch);


        ImageView listImage_imageView8 = (ImageView) findViewById(R.id.imageView8);
        Animation ListRiseAnimation_imageView8 = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_imge_eight);
        listImage_imageView8.startAnimation(ListRiseAnimation_imageView8);


        SeekBar list_timeRepeatBar = (SeekBar) findViewById(R.id.timeRepeatBar);
        Animation ListRiseAnimation_timeRepeatBar = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_timerepeatbar);
        list_timeRepeatBar.startAnimation(ListRiseAnimation_timeRepeatBar);


        TextView listText_timeRepeatOutput = (TextView) findViewById(R.id.timeRepeatOutput);
        Animation ListRiseAnimation_timeRepeatOutput = AnimationUtils.loadAnimation(this, R.anim.list_animeichen_timerepeatbar);
        listText_timeRepeatOutput.startAnimation(ListRiseAnimation_timeRepeatOutput);





        ((Switch) findViewById(R.id.plusSwitch)).setChecked(SharPref.getBoolean("PLUS_MODE", false));
        ((Switch) findViewById(R.id.minusSwitch)).setChecked(SharPref.getBoolean("MINUS_MODE", false));
        ((Switch) findViewById(R.id.xSwitch)).setChecked(SharPref.getBoolean("X_MODE", false));
        ((Switch) findViewById(R.id.delSwitch)).setChecked(SharPref.getBoolean("DEL_MODE", false));

        ((SeekBar) findViewById(R.id.chosenBorderBar)).setProgress(value);
        ((TextView) findViewById(R.id.chosenBorderOutput)).setText("Значения до: " + String.valueOf(value + 1));

        ((SeekBar) findViewById(R.id.timeRepeatBar)).setProgress(timeDelta - 1);
        ((TextView) findViewById(R.id.timeRepeatOutput)).setText("Запуск каждые " + String.valueOf(timeDelta) + " мин.");

        ((Switch) findViewById(R.id.plusSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor ed = SharPref.edit();
                if (isChecked) {
                    ed.putBoolean("PLUS_MODE", true);
                    ed.apply();
                } else {
                    ed.putBoolean("PLUS_MODE", false);
                    ed.apply();
                }
            }
        });

        ((Switch) findViewById(R.id.minusSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor ed = SharPref.edit();
                if (isChecked) {
                    ed.putBoolean("MINUS_MODE", true);
                    ed.apply();
                } else {
                    ed.putBoolean("MINUS_MODE", false);
                    ed.apply();
                }
            }
        });

        ((Switch) findViewById(R.id.xSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor ed = SharPref.edit();
                if (isChecked) {
                    ed.putBoolean("X_MODE", true);
                    ed.apply();
                } else {
                    ed.putBoolean("X_MODE", false);
                    ed.apply();
                }
            }
        });

        ((Switch) findViewById(R.id.delSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor ed = SharPref.edit();
                if (isChecked) {
                    ed.putBoolean("DEL_MODE", true);
                    ed.apply();
                } else {
                    ed.putBoolean("DEL_MODE", false);
                    ed.apply();
                }
            }
        });


        ((SeekBar) findViewById(R.id.chosenBorderBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = progress;
                ((TextView) findViewById(R.id.chosenBorderOutput)).setText("Значения до: " + String.valueOf(progress + 1));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor edit = SharPref.edit();
                edit.putInt("BORDER_VALUE", value);
                edit.apply();
            }
        });

        ((SeekBar) findViewById(R.id.timeRepeatBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeDelta = progress + 1;
                ((TextView) findViewById(R.id.timeRepeatOutput)).setText("Запуск каждые " + String.valueOf(progress + 1) + " мин.");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor edit = SharPref.edit();
                edit.putInt("TIME_VALUE", timeDelta);
                edit.apply();
            }
        });

        findViewById(R.id.goAppListBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.example.smartblock.activity_apps"));
            }
        });

        findViewById(R.id.goOsListBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.example.smartblock.activity_os_list"));
            }
        });


        findViewById(R.id.completeItBut).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {


                if (!mDPM.isAdminActive(mAdminName)) {
                    // try to become active
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Подтвердите, чтобы предотвратить удаление по неосторожности");
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    if (checkForPermission(SettingsActivity.this)) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (!Settings.canDrawOverlays(SettingsActivity.this)) {
                                Intent intentSett = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                SettingsActivity.this.startActivity(intentSett);
                            } else {
                                SharedPreferences.Editor editor = SharPref.edit();
                                editor.putString("CURRENT_APP_STATE", "ON");
                                editor.apply();
                                startBlocker();
                            }
                        } else {
                            SharedPreferences.Editor editor = SharPref.edit();
                            editor.putString("CURRENT_APP_STATE", "ON");
                            editor.apply();
                            startBlocker();
                        }
                    } else {
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    }
                }
            }
        });
    }


    private void startBlocker() {
        Intent serviceIntent = new Intent(this, AppLaunchDetectionService.class);

        //if (Build.VERSION.SDK_INT >= 26) this.startForegroundService(serviceIntent);
        //else this.startService(serviceIntent);

        //if (isMyServiceRunning(AppLaunchDetectionService.class))
        //stopService(new Intent(this, AppLaunchDetectionService.class));

        if (Build.VERSION.SDK_INT >= 26) this.startForegroundService(serviceIntent);
        else this.startService(serviceIntent);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        assert appOps != null;
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return mode == MODE_ALLOWED;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE == requestCode) {
            if (requestCode == Activity.RESULT_OK) {
                // done with activate to Device Admin
            } else {
                // cancel it.
            }
        }
    }
}
