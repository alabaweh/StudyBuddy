package com.example.studybuddy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class CreateSessionActivity extends AppCompatActivity {
    private EditText titleInput;
    private Button datePickerButton;
    private Button startTimeButton;
    private Button endTimeButton;
    private EditText locationInput;
    private Button createButton;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);

        titleInput = findViewById(R.id.sessionTitleInput);
        datePickerButton = findViewById(R.id.datePickerButton);
        startTimeButton = findViewById(R.id.startTimeButton);
        endTimeButton = findViewById(R.id.endTimeButton);
        locationInput = findViewById(R.id.locationInput);
        createButton = findViewById(R.id.createSessionButton);
        selectedDate = Calendar.getInstance();

        datePickerButton.setOnClickListener(v -> showDatePicker());
        startTimeButton.setOnClickListener(v -> showTimePicker(true));
        endTimeButton.setOnClickListener(v -> showTimePicker(false));
        createButton.setOnClickListener(v -> createSession());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateButtonText();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker(boolean isStartTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    if (isStartTime) {
                        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDate.set(Calendar.MINUTE, minute);
                        updateStartTimeButtonText();
                    } else {
                        // Store end time
                        updateEndTimeButtonText(hourOfDay, minute);
                    }
                },
                selectedDate.get(Calendar.HOUR_OF_DAY),
                selectedDate.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void updateDateButtonText() {
        datePickerButton.setText(String.format("%d/%d/%d",
                selectedDate.get(Calendar.MONTH) + 1,
                selectedDate.get(Calendar.DAY_OF_MONTH),
                selectedDate.get(Calendar.YEAR)));
    }

    private void updateStartTimeButtonText() {
        startTimeButton.setText(String.format("%02d:%02d",
                selectedDate.get(Calendar.HOUR_OF_DAY),
                selectedDate.get(Calendar.MINUTE)));
    }

    private void updateEndTimeButtonText(int hourOfDay, int minute) {
        endTimeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    private void createSession() {
        String title = titleInput.getText().toString().trim();
        String location = locationInput.getText().toString().trim();

        if (title.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save session to database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        long result = dbHelper.createSession(title, selectedDate.getTime(), location);

        if (result != -1) {
            Toast.makeText(this, "Session created successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to create session", Toast.LENGTH_SHORT).show();
        }
    }
}