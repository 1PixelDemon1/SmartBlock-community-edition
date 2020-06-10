package com.example.smartblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnDelete extends BroadcastReceiver {

    String TAG = "AAAAAAA";


        @Override
        public void onReceive(Context context, Intent intent) {
            // fetching package names from extras
            String[] packageNames = intent.getStringArrayExtra("android.intent.extra.PACKAGES");

            if(packageNames!=null){
                for(String packageName: packageNames){
                    if(packageName!=null && packageName.equals("com.example.smartblock")){
                        // User has selected our application under the Manage Apps settings
                        // now initiating background thread to watch for activity
                        new ListenActivities(context).start();
                    }
                }
            }
    }
}
