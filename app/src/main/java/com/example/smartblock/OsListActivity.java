package com.example.smartblock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class OsListActivity extends AppCompatActivity {

    private int Position = 0;
    private AlertDialog alertDialog;
    private TextView textView;
    final private String[] osNames = new String[] {
            "Huawei EMUI", "Xiaomi MIUI", "Samsung", "OPPO ColorOS", "VIVO Funtouch OS",
            "MEIZU Flyme", "Le EUI", "Smartisan OS"};

    final private String[] manNames = new String[] {
            "huawei", "xiaomi", "samsung", "oppo", "vivo",
            "meizu", "le", "smartisan"};

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_list);
        textView = findViewById(R.id.onc);
        name = Build.MANUFACTURER.toLowerCase();
        textView.setText(name);

        ((TextView)findViewById(R.id.onc)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                buildDialog();
            }
        });
        fillActions();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void buildDialog() {


        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(OsListActivity.this)) {
                Intent intentSett = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                OsListActivity.this.startActivity(intentSett);
                return;
        }


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.dialog_os_list, null);
        alertDialog = new AlertDialog.Builder(this).setView(view).create();

        if (Build.VERSION.SDK_INT < 26) {
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        } else {
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        final ListView listView = alertDialog.findViewById(R.id.listView);





        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, osNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Position = position;
                textView.setText(osNames[position]);
                name = manNames[position];
                fillActions();
                alertDialog.dismiss();

            }
        });

        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        alertDialog.getWindow().setAttributes(lp);
    }


    private void fillActions() {
        TextView actionBox = findViewById(R.id.wtdText);

        switch(name){
            case "huawei":
                actionBox.setText("1) Откройте \"Настройки>>Батарея>>Запуск приложений\"\n\n2) Найдите \"SmartBlock\" и отключите переключатель\n\n3) Включите переключатели: Автозапуск, Косвенный запуск, Работа в фоновом режиме");
                break;
            case "xiaomi":
                actionBox.setText("1) Найдите и запустите приложение \"Безопасность\", Нажмите \"Разрешения\"\n\n2) Найдите и нажмите на \"Автозапуск\"\n\n3) Найдите \"SmartBlock\" и активируйте переключатель");
                break;
            case "samsung":
                actionBox.setText("1) Откройте \"SmartManager\"\n\n2) Смахните вправо и выберите \"Автозапуск приложений\"\n\n3) Найдите \"SmartBlock\" и активируйте переключатель");
                break;
            case "meizu":
                actionBox.setText("1) Откройте \"Безопасность\"\n\n2) Нажмите \"Разрешения\"\n\n3) Выберите \"Управление приложениями\"\n\n4) Найдите \"SmartBlock\" и разрешите приложению работать в фоновом режиме");
                break;
            case "oppo":
                actionBox.setText("1) Откройте \"Управление телефоном\" и выберите \"Разрешения\"\n\n2) Откройте \"Менеджер запуска\"\n\n3) Найдите \"SmartBlock\" и активируйте переключатель");
                break;
            case "vivo":
                actionBox.setText("1) Откройте \"i Manager\" и откройте \"Управление приложениями\"\n\n2) Откройте \"Автозапуск\"\n\n3) Найдите \"SmartBlock\"  и активируйте переключатель");
                break;
            case "le":
                actionBox.setText("1) Откройте \"Настройки\", перейдите в раздел \"Конфиденциальность\"\n\n2) Выберите \"Автозапуск\"\n\n3) Найдите \"SmartBlock\" и активируйте переключатель");
                break;
            case "smartisan":
                actionBox.setText("1) Откройте \"Упрвление телефоном\", нажмите \"Управление разрешениями\"\n\n2) Нажмите на \"Разрешения на автозапуск\"\n\n3) Найдите \"SmartBlock\" и активируйте переключатель \"Разрешать запуск системой\"");
                break;
            default:
                actionBox.setText("Нет необходимости в каких-либо дополнительных действиях");
        }
    }

}
