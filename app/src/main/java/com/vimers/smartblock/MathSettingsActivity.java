package com.vimers.smartblock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MathSettingsActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    private Switch addition;
    private Switch subtraction;
    private Switch multiplication;
    private Switch division;

    private EditText fromAdditionInput;
    private EditText toAdditionInput;
    private EditText fromSubtractionInput;
    private EditText toSubtractionInput;
    private EditText fromMultiplicationInput;
    private EditText toMultiplicationInput;
    private EditText toDivisionInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_settings);

        sharedPreferences = getSharedPreferences("MATH_SETTINGS", MODE_PRIVATE);

        (addition = findViewById(R.id.additionSwitch)).setOnCheckedChangeListener(this::onCheckedChanged);
        (subtraction = findViewById(R.id.subtractionSwitch)).setOnCheckedChangeListener(this::onCheckedChanged);
        (multiplication = findViewById(R.id.multiplaicationSwitch)).setOnCheckedChangeListener(this::onCheckedChanged);
        (division = findViewById(R.id.divisionSwitch)).setOnCheckedChangeListener(this::onCheckedChanged);

        (fromAdditionInput = findViewById(R.id.fromAdditionInput)).setText(String.format("%d", sharedPreferences.getInt("ADDITION_FROM", 0)));
        (toAdditionInput = findViewById(R.id.toAdditionInput)).setText(String.format("%d", sharedPreferences.getInt("ADDITION_TO", 100)));
        (fromSubtractionInput = findViewById(R.id.fromSubtractionInput)).setText(String.format("%d", sharedPreferences.getInt("SUBTRACTION_FROM", 0)));
        (toSubtractionInput = findViewById(R.id.toSubtractionInput)).setText(String.format("%d", sharedPreferences.getInt("SUBTRACTION_TO", 100)));
        (fromMultiplicationInput = findViewById(R.id.fromMultiplicationInput)).setText(String.format("%d", sharedPreferences.getInt("MULTIPLICATION_FROM", 0)));
        (toMultiplicationInput = findViewById(R.id.toMultiplicationInput)).setText(String.format("%d", sharedPreferences.getInt("MULTIPLICATION_TO", 15)));
        (toDivisionInput = findViewById(R.id.toDivisionInput)).setText(String.format("%d", sharedPreferences.getInt("DIVISION_TO", 60)));
        fillViews();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareNumbersInsideInput(fromAdditionInput, toAdditionInput);
                compareNumbersInsideInput(fromSubtractionInput, toSubtractionInput);
                compareNumbersInsideInput(fromMultiplicationInput, toMultiplicationInput);
                if(toDivisionInput.getText().equals("")) toDivisionInput.setText("1");
                saveAll();
            }
        });
    }

    private void fillViews() {
        fromAdditionInput.setEnabled(addition.isChecked());
        toAdditionInput.setEnabled(addition.isChecked());
        fromSubtractionInput.setEnabled(subtraction.isChecked());
        toSubtractionInput.setEnabled(subtraction.isChecked());
        fromMultiplicationInput.setEnabled(multiplication.isChecked());
        toMultiplicationInput.setEnabled(multiplication.isChecked());
        toDivisionInput.setEnabled(division.isChecked());

        addition.setChecked(sharedPreferences.getBoolean("ADDITION", false));
        subtraction.setChecked(sharedPreferences.getBoolean("SUBTRACTION", false));
        multiplication.setChecked(sharedPreferences.getBoolean("MULTIPLICATION", false));
        division.setChecked(sharedPreferences.getBoolean("DIVISION", false));
    }

    private void onCheckedChanged(CompoundButton switchView, boolean isChecked) {
        if(switchView == addition) {
            sharedPreferences.edit().putBoolean("ADDITION", addition.isChecked()).apply();
            ((EditText) findViewById(R.id.fromAdditionInput)).setEnabled(addition.isChecked());
            ((EditText) findViewById(R.id.toAdditionInput)).setEnabled(addition.isChecked());
        }
        if(switchView == subtraction) {
            sharedPreferences.edit().putBoolean("SUBTRACTION", subtraction.isChecked()).apply();
            ((EditText) findViewById(R.id.fromSubtractionInput)).setEnabled(subtraction.isChecked());
            ((EditText) findViewById(R.id.toSubtractionInput)).setEnabled(subtraction.isChecked());
        }
        if(switchView == multiplication) {
            sharedPreferences.edit().putBoolean("MULTIPLICATION", multiplication.isChecked()).apply();
            ((EditText) findViewById(R.id.fromMultiplicationInput)).setEnabled(multiplication.isChecked());
            ((EditText) findViewById(R.id.toMultiplicationInput)).setEnabled(multiplication.isChecked());
        }
        if(switchView == division) {
            sharedPreferences.edit().putBoolean("DIVISION", division.isChecked()).apply();
            ((EditText) findViewById(R.id.toDivisionInput)).setEnabled(division.isChecked());
        }
    }

    private void saveAll() {
        sharedPreferences.edit().putInt("ADDITION_FROM", Integer.parseInt(fromAdditionInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("ADDITION_TO", Integer.parseInt(toAdditionInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("ADDITION_FROM", Integer.parseInt(fromAdditionInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("ADDITION_TO", Integer.parseInt(toAdditionInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("SUBTRACTION_FROM", Integer.parseInt(fromSubtractionInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("SUBTRACTION_TO", Integer.parseInt(toSubtractionInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("SUBTRACTION_FROM", Integer.parseInt(fromSubtractionInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("SUBTRACTION_TO", Integer.parseInt(toSubtractionInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("MULTIPLICATION_FROM", Integer.parseInt(fromMultiplicationInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("MULTIPLICATION_TO", Integer.parseInt(toMultiplicationInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("MULTIPLICATION_FROM", Integer.parseInt(fromMultiplicationInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("MULTIPLICATION_TO", Integer.parseInt(toMultiplicationInput.getText().toString())).apply();
        sharedPreferences.edit().putInt("DIVISION_TO", Integer.parseInt(toDivisionInput.getText().toString())).apply();
    }

    private void compareNumbersInsideInput(EditText from, EditText to) {
        String var;
        if(from.getText().equals("")) from.setText("1");
        if(to.getText().equals("")) to.setText("1");
        if(!(Integer.parseInt(from.getText().toString()) <= Integer.parseInt(to.getText().toString()))) {
            var = to.getText().toString();
            to.setText(from.getText());
            from.setText(var);
        }
    }
}
