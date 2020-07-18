package com.vimers.smartblock;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

public class DialogBlockerManager {
    private static MathExercise mathExercise;
    public static boolean isActive;
    public static int numberOfExercises;
    @Nullable
    private DialogDisplayService dialogDisplayService;
    private final ServiceConnection dialogDisplayServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dialogDisplayService = ((DialogDisplayService.Binder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dialogDisplayService = null;
        }
    };

    DialogBlockerManager(Context appContext) {
        Intent dialogServiceIntent = new Intent(appContext, DialogDisplayService.class);
        appContext.bindService(
                dialogServiceIntent,
                dialogDisplayServiceConn,
                Context.BIND_AUTO_CREATE
        );
    }

    //Builds Alert dialog and returns it
    public static AlertDialog getDialog() {
        Context context;
        if ((context = MainActivity.getContextOfApplication()) == null) {
            context = DialogDisplayService.getContext();
        }

        fillMathExercise(context);
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_blocker_layout, null);
        numberOfExercises = context.getSharedPreferences("MATH_SETTINGS", Context.MODE_PRIVATE).getInt("NUMBER_OF_EXERCISES", 1);
        builder.setView(view)
                .setCancelable(false)
                .setTitle("AlertDialog")
                .setMessage("Message")
                .setPositiveButton("OK BUTTON", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        if (Build.VERSION.SDK_INT < 26) {
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        } else {
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }

    //Returns activities to do when button clicked
    public OnClickListener getButtonAction(AlertDialog alertDialog) {

        ((EditText) alertDialog.findViewById(R.id.answerInput)).setOnKeyListener(new View.OnKeyListener() { //When "Enter" is pressed we do the same as if we pressed ok
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    buttonAction(alertDialog);
                    return true;
                }
                return false;
            }
        });

        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction(alertDialog);
            }
        };
    }
    //Fills AlertDialog fields
    public static void fillDialog(AlertDialog alertDialog) {
        ((TextView) alertDialog.findViewById(R.id.textField)).setText(mathExercise.getExerciseString());
    }
    //Cleans input EditText View
    public static void clearInput(AlertDialog alertDialog) {
        ((EditText) alertDialog.findViewById(R.id.answerInput)).setText("");
    }

    private static void fillMathExercise(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MATH_SETTINGS", Context.MODE_PRIVATE);
        List<MathActions> mathActions = new LinkedList<MathActions>();
        if (sharedPreferences.getBoolean("ADDITION", true)) mathActions.add(MathActions.ADDITION);
        if (sharedPreferences.getBoolean("SUBTRACTION", false))
            mathActions.add(MathActions.SUBTRACTION);
        if (sharedPreferences.getBoolean("MULTIPLICATION", false))
            mathActions.add(MathActions.MULTIPLICATION);
        if (sharedPreferences.getBoolean("DIVISION", false)) mathActions.add(MathActions.DIVISION);
        mathExercise = new MathExercise(mathActions);
    }

    //Actually button action
    private void buttonAction(AlertDialog alertDialog) {
        int answer;
        try {
            answer = Integer.parseInt(((EditText) alertDialog.findViewById(R.id.answerInput)).getText().toString());
        } catch (Exception e) {
            answer = 0;
        }
        if (mathExercise.isCorrect(answer) || answer == 1) { //TODO DELETE ELSE STATEMENT
            if (--numberOfExercises <= 0) {
                alertDialog.dismiss();
                isActive = false;
                dialogDisplayService.resetTimer();
            } else {
                clearInput(alertDialog);
                fillDialog(alertDialog);
            }
        }
    }
}