package com.vimers.smartblock;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.lang3.RandomStringUtils;

import static android.content.Context.MODE_PRIVATE;

public class PasswordChecker {
    private static String passwordLine = "";
    private static SharedPreferences passwordPreference;

    //Checks if given line is equivalent to password line
    static public boolean isCorrect(String password) {
        if(passwordLine.equals("")) {
            reset();
        }
        return password.equals(passwordLine);
    }
    //Re-sets SharedPreferences and defines password line
    static public void reset() {
        Context applicationContext;
        if((applicationContext = MainActivity.getContextOfApplication()) == null) {
            applicationContext = DialogDisplayService.getContext();
        }
        passwordPreference = applicationContext.getSharedPreferences("PASSWORD_LINE", MODE_PRIVATE);
        passwordLine = passwordPreference.getString("PASSWORD", "");
    }
    //Sets password
    static public void setPassword(String password) {
        if(passwordPreference == null) {
            reset();
        }
        passwordLine = password;
        passwordPreference.edit().putString("PASSWORD", password).apply();
    }
    //Generates new Password
    static public String generateNewPassword() {
        reset();
        String newPassword = RandomStringUtils.random(passwordLine.length(), true, true);
        setPassword(newPassword);
        return newPassword;
    }
}
