package com.example.smartblock;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppsActivity extends ListActivity {
    private static Set<String> packageNames;
    public SharedPreferences sp;
    public SharedPreferences ba;
    private PackageManager packageManager;
    private List appList;
    private AppAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);


        packageNames = new HashSet<>();
        packageManager = getPackageManager();
        ba = getSharedPreferences("block_apps", MODE_PRIVATE);
        sp = getSharedPreferences("settings", MODE_PRIVATE);
        packageNames = ba.getStringSet("blocked_apps", new HashSet<String>());

        new LoadApplications().execute();

        (findViewById(R.id.completeActionButton)).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //autoBlock();
                showPopupMenu(v);
            }
        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_apps, menu);
//        Log.d("AAA", "onCreateOptionsMenu: ");
//        return true;
//    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu_apps);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_auto:
                                autoBlock();
                                return true;
                            case R.id.action_turn_off:
                                turnOffAll();
                                return true;
                            case R.id.action_turn_on:
                                turnOnAll();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.show();
    }


    protected void turnOffAll() {
        ba = getSharedPreferences("block_apps", MODE_PRIVATE);
        packageNames = ba.getStringSet("blocked_apps", new HashSet<String>());

        packageNames.clear();
        SharedPreferences.Editor edit = ba.edit();
        edit.clear();
        edit.putStringSet("blocked_apps", packageNames);
        edit.apply();

        this.recreate();
    }

    protected void turnOnAll() {
        ba = getSharedPreferences("block_apps", MODE_PRIVATE);
        packageNames = ba.getStringSet("blocked_apps", new HashSet<String>());
        packageNames.clear();

        for (int i = 0; i < appList.size(); i++) {
            ApplicationInfo app = (ApplicationInfo) appList.get(i);
            packageNames.add(app.packageName);
        }

        SharedPreferences.Editor edit = ba.edit();
        edit.clear();
        edit.putStringSet("blocked_apps", packageNames);
        edit.apply();

        this.recreate();
    }

    protected void autoBlock() {
        ba = getSharedPreferences("block_apps", MODE_PRIVATE);
        packageNames = ba.getStringSet("blocked_apps", new HashSet<String>());

        for (int i = 0; i < appList.size(); i++) {
            ApplicationInfo app = (ApplicationInfo) appList.get(i);
            if (app.packageName.indexOf("android") == -1) {
                if (!packageNames.contains(app.packageName)) {
                    packageNames.add(app.packageName);
                }
            }
        }

        for (int i = 0; i < appList.size(); i++) {
            ApplicationInfo app = (ApplicationInfo) appList.get(i);
            if (app.packageName.indexOf("wallpaper") != -1 || app.packageName.indexOf("Wallpaper") != -1) {
                if (packageNames.contains(app.packageName)) {
                    packageNames.remove(app.packageName);
                }
            }
        }

        for (int i = 0; i < appList.size(); i++) {
            ApplicationInfo app = (ApplicationInfo) appList.get(i);
            if (app.packageName.indexOf("google") != -1 || app.packageName.indexOf("Google") != -1) {
                if (!packageNames.contains(app.packageName)) {
                    packageNames.add(app.packageName);
                }
            }
        }

        for (int i = 0; i < appList.size(); i++) {
            ApplicationInfo app = (ApplicationInfo) appList.get(i);
            if (app.packageName.indexOf("chrome") != -1) {
                if (!packageNames.contains(app.packageName)) packageNames.add(app.packageName);
                break;
            }
        }

        packageNames.remove("com.example.smartblock");


        SharedPreferences.Editor editor = ba.edit();
        editor.clear();
        editor.putStringSet("blocked_apps", packageNames);
        editor.apply();

        this.recreate();
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Switch OnOff = v.findViewById(R.id.OnOff);
        ApplicationInfo app = (ApplicationInfo) appList.get(position);
        ba = getSharedPreferences("block_apps", MODE_PRIVATE);
        packageNames = ba.getStringSet("blocked_apps", new HashSet<String>());

        String text = app.packageName;


        if (packageNames.contains(text)) {
            packageNames.remove(text);
            SharedPreferences.Editor e = ba.edit();
            e.clear();
            e.putStringSet("blocked_apps", packageNames);
            e.apply();
            OnOff.setChecked(false);
        } else {
            packageNames.add(text);
            SharedPreferences.Editor e = ba.edit();
            e.clear();
            e.putStringSet("blocked_apps", packageNames);
            e.apply();
            OnOff.setChecked(true);
        }
    }


    private class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            appList = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));

            listAdapter = new AppAdapter(AppsActivity.this, R.layout.activity_list, appList);

            return null;
        }

        private List checkForLaunchIntent(List list) {
            ArrayList appList = new ArrayList();

            for (Object info : list) {
                try {
                    appList.add(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return appList;
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listAdapter);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AppsActivity.this, null, "Loading apps info...");
            super.onPreExecute();
        }
    }


}
