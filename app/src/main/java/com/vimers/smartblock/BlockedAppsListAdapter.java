package com.vimers.smartblock;

import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import java.util.List;
import java.util.Set;

import android.content.Context;


public class BlockedAppsListAdapter extends RecyclerView.Adapter<BlockedAppsListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List appList;
    private PackageManager packageManager;
    private View.OnClickListener onClickListener;
    private Context context;
    private Set<String> packageNames;
    private int appListElementSecondColor = Color.argb(100,  162, 241, 255);
    //Constructor. Defines all variables
    BlockedAppsListAdapter(Context context, List appList) {
        this.appList = appList;
        this.inflater = LayoutInflater.from(context);
        this.packageManager = context.getPackageManager();
        this.context = context;
        packageNames = BlockedAppsListManager.getPackageNames();
        onClickListener = new View.OnClickListener() {//implementing an onClickListener for every single list element to call onItemClicked()
            @Override
            public void onClick(View v) {
                onItemClicked(v);
            }
        };
    }
    //Creates a single List item
    @Override
    public BlockedAppsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.blocked_apps_list_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }
    //Fills List item elements
    @Override
    public void onBindViewHolder(BlockedAppsListAdapter.ViewHolder holder, int position) {
        ApplicationInfo app = (ApplicationInfo) appList.get(position);
        holder.appIcon.setImageDrawable(app.loadIcon(packageManager));
        holder.appName.setText(app.loadLabel(packageManager));
        if(packageNames.contains(app.packageName)) {
            holder.appSwitch.setChecked(true);
            holder.linearLayout.setBackgroundColor(appListElementSecondColor);
        }
        else {//Else is necessary for soft working during scrolling
            holder.appSwitch.setChecked(false);
            holder.linearLayout.setBackgroundColor(holder.linearLayout.getSolidColor());
        }
    }
    //Returns length of appList List
    @Override
    public int getItemCount() {
        return appList.size();
    }
    //Adds and removes package names from global package name container
    private void onItemClicked(final View view) {
        int itemPosition = (AppListActivity.recyclerView).getChildAdapterPosition(view);
        String packageName = ((ApplicationInfo) appList.get(itemPosition)).packageName;
        if(packageNames.contains(packageName)) {
            BlockedAppsListManager.removeApp(packageName);//removing package name from list in global class
            packageNames = BlockedAppsListManager.getPackageNames();//re-setting package names set
            ((Switch) view.findViewById(R.id.appSwitch)).setChecked(false);//Setting a switch off
            ((LinearLayout)view.findViewById(R.id.listItemLayout)).setBackgroundColor(view.findViewById(R.id.listItemLayout).getSolidColor());//Changing color to (change to color you need)
        }
        else {
            BlockedAppsListManager.addApp(packageName);//adding package name from list in global class
            packageNames = BlockedAppsListManager.getPackageNames();//re-setting package names set
            ((Switch) view.findViewById(R.id.appSwitch)).setChecked(true);//setting a switch on
            ((LinearLayout)view.findViewById(R.id.listItemLayout)).setBackgroundColor(appListElementSecondColor);//Changing color to default
        }
    }
    //Bonds layout with single List element
    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView appIcon;
        final TextView appName;
        final Switch appSwitch;
        final LinearLayout linearLayout;

        ViewHolder(View view){
            super(view);
            appIcon = (ImageView)view.findViewById(R.id.appIcon);
            appName = (TextView) view.findViewById(R.id.appName);
            appSwitch = (Switch) view.findViewById(R.id.appSwitch);
            linearLayout = (LinearLayout) view.findViewById(R.id.listItemLayout);
        }
    }
}
