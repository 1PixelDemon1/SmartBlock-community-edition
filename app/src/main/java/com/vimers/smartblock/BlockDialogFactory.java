package com.vimers.smartblock;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.vimers.smartblock.exercises.Exercise;

public final class BlockDialogFactory {
    private BlockDialogFactory() {
    }

    public static AlertDialog createDialog(Context context, Exercise exercise) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        View dialogView = inflateDialogView(context);
        alertBuilder
                .setView(dialogView)
                .setCancelable(false)
                .setTitle("Упражнение")
                .setMessage(exercise.getQuestion());
        AlertDialog dialog = alertBuilder.create();
        makeDialogAppearAnywhere(dialog);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private static void makeDialogAppearAnywhere(AlertDialog dialog) {
        int globalDialogParam = (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) ?
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT :
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        dialog.getWindow().setType(globalDialogParam);
    }

    private static View inflateDialogView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.dialog_blocker_layout, null);
    }
}