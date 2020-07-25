package com.vimers.smartblock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.vimers.smartblock.persistence.PersistentObject;

public class InputPasswordActivity extends AppCompatActivity {
    private Intent whereToGoIfCorrect;
    private EditText passwordField;

    private PersistentObject<AppSettings> appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);
        passwordField = findViewById(R.id.passwordInputEdit);
        whereToGoIfCorrect = new Intent(getIntent().getStringExtra("INTENT")); //Intent, which contains next going activity.(Where to go if password is correct)
        appSettings = new PersistentObject<>(
                this,
                AppSettings.PERSISTENT_OBJECT_NAME,
                AppSettings.class
        );

        findViewById(R.id.checkPasswordButton).setOnClickListener(v -> checkPassword());
    }

    @Override
    protected void onNewIntent(Intent intent) { //When Activity is launched again, new intent is given
        super.onNewIntent(intent);
        whereToGoIfCorrect = new Intent(intent.getStringExtra("INTENT"));
    }

    private void checkPassword() {  //Checks if password is correct and sends user to his destination
        passwordField.setText("");
        String inPassword = passwordField.getText().toString();
        String actualPassword = appSettings.getObj().getPassword();
        if (inPassword.equals(actualPassword)) {
            finish();
            startActivity(whereToGoIfCorrect);
        }
    }
}
