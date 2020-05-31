package com.vimers.smartblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class RegistrationActivity extends AppCompatActivity {

    private EditText childNameEdit;
    private EditText passwordEdit;
    private Button completeRegistrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        childNameEdit = findViewById(R.id.childNameEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        completeRegistrationButton = findViewById(R.id.completeRegistrationBut);
        //TODO delete this line
        completeRegistrationButton.setEnabled(true);
        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                savePassword();
                enableButton();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };

        childNameEdit.addTextChangedListener(textWatcher);
        passwordEdit.addTextChangedListener(textWatcher);

        completeRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegistrationCompleted();
            }
        });
    }
    //Saves inputted password
    private void savePassword() {
        PasswordChecker.setPassword(passwordEdit.getText().toString());
    }
    //Enables or not enables continue button
    private void enableButton() {
        completeRegistrationButton.setEnabled(childNameEdit.length() != 0 && passwordEdit.length()!= 0);
    }
    //OnClick action
    private void onRegistrationCompleted() {
        try {
            stopService(new Intent(this ,DialogDisplayService.class));
            startService(new Intent(this ,DialogDisplayService.class));
        }
        catch (Exception e) {
            startService(new Intent(this ,DialogDisplayService.class));
        }

        //startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        //new MailSender("Здравствйте, уважаемые родители", "Ваш ребенок, Ярослав попытался удалить SmartBlock", "jarafan3@gmail.com").sendMail();
        startActivity(new Intent("com.vimers.smartblock.activity_app_list"));
        //startActivity(new Intent("com.vimers.smartblock.activity_input_password").putExtra("INTENT", "com.vimers.smartblock.activity_app_list"));
    }

}
