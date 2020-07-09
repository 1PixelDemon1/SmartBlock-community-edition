package com.vimers.smartblock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class DialogBlockerManager {
    private static MathExercise mathExercise;
    public static boolean isActive = false;
    public static int numberOfExercises;

    //Builds Alert dialog and returns it
    public static AlertDialog getDialog() {
        Context context;
        if((context = MainActivity.getContextOfApplication()) == null) {
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
    public static OnClickListener getButtonAction(final AlertDialog alertDialog) {

        ((EditText) alertDialog.findViewById(R.id.answerInput)).setOnKeyListener(new View.OnKeyListener() { //When "Enter" is pressed we do the same as if we pressed ok
            public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
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
        if(sharedPreferences.getBoolean("ADDITION", true)) mathActions.add(MathActions.ADDITION);
        if(sharedPreferences.getBoolean("SUBTRACTION", false)) mathActions.add(MathActions.SUBTRACTION);
        if(sharedPreferences.getBoolean("MULTIPLICATION", false)) mathActions.add(MathActions.MULTIPLICATION);
        if(sharedPreferences.getBoolean("DIVISION", false)) mathActions.add(MathActions.DIVISION);
        mathExercise = new MathExercise(mathActions);
    }
    //Actually button action
    private static void buttonAction(AlertDialog alertDialog) {
        int answer;
        try{
            answer = Integer.parseInt(((EditText) alertDialog.findViewById(R.id.answerInput)).getText().toString());
        }
        catch (Exception e) {
            answer = 0;
        }
        if (mathExercise.isCorrect(answer) || answer == 1) { //TODO DELETE ELSE STATEMENT
            if(--numberOfExercises <= 0) {
                alertDialog.dismiss();
                isActive = false;
                DialogDisplayService.resetTimer();
            } else {
                clearInput(alertDialog);
                fillDialog(alertDialog);
            }
        }
    }
}