package com.vimers.smartblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InputPasswordActivity extends AppCompatActivity {
    private Intent whereToGoIfCorrect;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);
        passwordField = findViewById(R.id.passwordInputEdit);
        whereToGoIfCorrect = new Intent(this.getIntent().getStringExtra("INTENT")); //Intent, which contains next going activity.(Where to go if password is correct)
        findViewById(R.id.checkPasswordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) { //When Activity is launched again, new intent is given
        super.onNewIntent(intent);
        whereToGoIfCorrect = new Intent(intent.getStringExtra("INTENT"));
    }

    private void checkPassword() {  //Checks if password is correct and sends user to his destination
        if(PasswordChecker.isCorrect(passwordField.getText().toString())) {
            passwordField.setText("");
            finish();
            startActivity(whereToGoIfCorrect);
        }
        else {
            passwordField.setText("");
        }
    }
}
