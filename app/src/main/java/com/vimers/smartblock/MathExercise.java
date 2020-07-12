package com.vimers.smartblock;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MathExercise {
    private List<MathActions> chosenActivities;
    private int answer;
    private int borderNumber = 100;
    private Context context;
    private SharedPreferences sharedPreferences;

    //TODO separate for every MathAction
    //Constructor fills a field of Array of MathActivities, which will be displayed
    MathExercise(List<MathActions> chosenActivities) {
        this.chosenActivities = chosenActivities;
        if((context = MainActivity.getContextOfApplication()) == null) {
            context = DialogDisplayService.getContext();
        }
        sharedPreferences = context.getSharedPreferences("MATH_SETTINGS", Context.MODE_PRIVATE);
    }
    //Generates a Math Exercise and returns a string. Answer is written in Object field
    public String getExerciseString() {
        int first;
        int second;
        int borderFrom;

        if(chosenActivities.size() == 0) {
            borderFrom = sharedPreferences.getInt("ADDITION_FROM", 1);
            first = new Random().nextInt(sharedPreferences.getInt("ADDITION_TO", 100) - borderFrom + 1) + borderFrom ;
            second = new Random().nextInt(sharedPreferences.getInt("ADDITION_TO", 100) - borderFrom + 1) + borderFrom;
            answer = first + second;
            return first + " + " + second + " = ?";
        }

        MathActions action = chosenActivities.get(new Random().nextInt(chosenActivities.size()));
        String exercise = "";
        switch (action) {
            case ADDITION:
                borderFrom = sharedPreferences.getInt("ADDITION_FROM", 1);
                first = new Random().nextInt(sharedPreferences.getInt("ADDITION_TO", 100) - borderFrom + 1) + borderFrom;
                second = new Random().nextInt(sharedPreferences.getInt("ADDITION_TO", 100) - borderFrom + 1) + borderFrom;
                answer = first + second;
                exercise = first + " + " + second + " = ?";
                break;
            case SUBTRACTION:
                borderFrom = sharedPreferences.getInt("SUBTRACTION_FROM", 1);
                first = new Random().nextInt(sharedPreferences.getInt("SUBTRACTION_TO", 100) - borderFrom + 1) + borderFrom;
                second = new Random().nextInt(first + 1);
                answer = first - second;
                exercise = first + " - " + second + " = ?";
                break;
            case MULTIPLICATION:
                borderFrom = sharedPreferences.getInt("MULTIPLICATION_FROM", 1);
                first = new Random().nextInt(sharedPreferences.getInt("MULTIPLICATION_TO", 20) - borderFrom + 1) + borderFrom;
                second = new Random().nextInt(sharedPreferences.getInt("MULTIPLICATION_TO", 20) - borderFrom + 1) + borderFrom;
                answer = first * second;
                exercise = first + " × " + second + " = ?";
                break;
            case DIVISION:
                borderFrom = sharedPreferences.getInt("DIVISION_FROM", 1);
                first = new Random().nextInt(sharedPreferences.getInt("DIVISION_TO", 100) - borderFrom + 1) + borderFrom;
                ArrayList<Integer> secondsArray = new ArrayList<Integer>();
                for (int i = 1; i * i <= first; i++) {
                    if(first % i == 0)
                    {
                        if(i * i != first) secondsArray.add(i);
                        secondsArray.add(first / i);
                    }
                }
                Collections.sort(secondsArray);
                second = secondsArray.get(new Random().nextInt(secondsArray.size() - (secondsArray.size() == 2 || secondsArray.size() == 1? 0: 1))); //If number is prime, then it can be 1 or number. Else: number itself is disabled
                answer = first / second;
                exercise = first + " ÷ " + second + " = ?";
                break;
        }

        return exercise;
    }
    //Checks if given value is equal to answer of generated exercise
    public boolean isCorrect(int value) {
        return value == answer;
    }
}
