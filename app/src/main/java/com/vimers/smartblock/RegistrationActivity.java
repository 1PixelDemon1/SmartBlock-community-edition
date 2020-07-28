package com.vimers.smartblock;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.vimers.smartblock.persistence.PersistentObject;

import kotlin.Unit;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mailEdit;
    private EditText passwordEdit;
    private Button completeRegistrationButton;

    private PersistentObject<AppSettings> appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mailEdit = findViewById(R.id.mailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        completeRegistrationButton = findViewById(R.id.completeRegistrationBut);

        appSettings = new PersistentObject<>(this, AppSettings.class);

        TextWatcher textWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                completeRegistrationButton.setEnabled(isInputDataCorrect());
            }

            private boolean isInputDataCorrect() {
                return (mailEdit.length() != 0)
                        && (passwordEdit.length() != 0);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        mailEdit.addTextChangedListener(textWatcher);
        passwordEdit.addTextChangedListener(textWatcher);

        completeRegistrationButton.setOnClickListener(v -> onRegistrationCompleted());
    }

    /**
     * Saves the inputted password.
     */
    private void saveData() {
        appSettings.edit(settings -> {
            settings.setMail(mailEdit.getText().toString());
            settings.setPassword(passwordEdit.getText().toString());
            return Unit.INSTANCE;
        }).save();
    }

    private void onRegistrationCompleted() {
        saveData();
        getSharedPreferences("APP_STATE", MODE_PRIVATE).edit().putBoolean("REGISTRATION_COMPLETED", true).apply();
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
