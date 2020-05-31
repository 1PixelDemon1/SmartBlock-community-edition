package com.vimers.smartblock;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class AppDeviceAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        //
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        //TODO: delete comment section
        //new MailSender("Hello john", "Hi", "pxldem@gmail.com").sendMail();
        return context.getString(R.string.admin_disable_requested_warning);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        //TODO: send mail
    }
}
