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
        whereToGoIfCorrect = new Intent(this.getIntent().getStringExtra("INTENT"));
        findViewById(R.id.checkPasswordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        whereToGoIfCorrect = new Intent(intent.getStringExtra("INTENT"));
    }

    private void checkPassword() {
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
