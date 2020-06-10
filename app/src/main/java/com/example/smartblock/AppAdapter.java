package com.example.smartblock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppAdapter extends ArrayAdapter {
    private List appList;
    private Context context;
    private PackageManager packageManager;

    AppAdapter(Context context, int resource,
               List objects) {
        super(context, resource, objects);

        this.context = context;
        this.appList = objects;
        packageManager = context.getPackageManager();
    }

    @Override
    public int getCount() {
        return ((null != appList) ? appList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return (null != appList) ? (ApplicationInfo) appList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SharedPreferences sp = getContext().getSharedPreferences("block_apps", Context.MODE_PRIVATE);
        Set<String>  stringset = sp.getStringSet("blocked_apps", new HashSet<String>());

        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.activity_list, null);
        }

        ApplicationInfo data = (ApplicationInfo) appList.get(position);

        if (null != data) {
            TextView appName = view.findViewById(R.id.app_name);

            //TextView packageName = (TextView) view.findViewById(R.id.app_package);
            ImageView iconView = view.findViewById(R.id.app_icon);


            for(String text: stringset) {
                if(text.equals(data.packageName)) {
                    Switch OnOff = view.findViewById(R.id.OnOff);
                    OnOff.setChecked(true);
                    break;
                } else {
                    Switch OnOff = view.findViewById(R.id.OnOff);
                    OnOff.setChecked(false);
                }
            }

            appName.setText(data.loadLabel(packageManager));
            //packageName.setText(data.packageName);
            iconView.setImageDrawable(data.loadIcon(packageManager));
        }
        return view;
    }
}