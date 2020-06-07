package com.vimers.smartblock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class DialogBlockerManager {
    private static MathExercise mathExercise = new MathExercise(new MathActions[]{MathActions.DIVISION, MathActions.ADDITION, MathActions.MULTIPLICATION, MathActions.SUBTRACTION});
    public static boolean isActive = false;
    public static int numberOfExercises;

    //Builds Alert dialog and returns it
    public static AlertDialog getDialog() {
        Context context;
        //mathExercise
        if((context = MainActivity.getContextOfApplication()) == null) {
            context = DialogDisplayService.getContext();
        }
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
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
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
}