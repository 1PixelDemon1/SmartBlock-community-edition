package com.vimers.smartblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MathExercise {
    private List<MathActions> chosenActivities;
    private int answer;
    private int borderNumber = 100;

    //TODO separate for every MathAction
    //Constructor fills a field of Array of MathActivities, which will be displayed
    MathExercise(List<MathActions> chosenActivities) {
        this.chosenActivities = chosenActivities;
    }
    //Generates a Math Exercise and returns a string. Answer is written in Object field
    public String getExerciseString() {
        int first;
        int second;

        if(chosenActivities.size() == 0) {
            first = new Random().nextInt(borderNumber);
            second = new Random().nextInt(borderNumber);
            answer = first + second;
            return first + " + " + second + " = ?";
        }

        MathActions action = chosenActivities.get(new Random().nextInt(chosenActivities.size()));
        String exercise = "";
        switch (action) {
            case ADDITION:
                first = new Random().nextInt(borderNumber);
                second = new Random().nextInt(borderNumber);
                answer = first + second;
                exercise = first + " + " + second + " = ?";
                break;
            case SUBTRACTION:
                first = new Random().nextInt(borderNumber);
                second = new Random().nextInt(first);
                answer = first - second;
                exercise = first + " - " + second + " = ?";
                break;
            case MULTIPLICATION:
                first = new Random().nextInt(borderNumber);
                second = new Random().nextInt(borderNumber);
                answer = first * second;
                exercise = first + " × " + second + " = ?";
                break;
            case DIVISION:
                first = new Random().nextInt(borderNumber);
                ArrayList<Integer> secondsArray = new ArrayList<Integer>();
                for (int i = 1; i * i <= first; i++) {
                    if(first % i == 0)
                    {
                        if(i * i != first) secondsArray.add(i);
                        secondsArray.add(first / i);
                    }
                }
                Collections.sort(secondsArray);
                second = secondsArray.get(new Random().nextInt(secondsArray.size() - (secondsArray.size() == 2? 0: 1))); //If number is prime, then it can be 1 or number. Else: number itself is disabled
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
