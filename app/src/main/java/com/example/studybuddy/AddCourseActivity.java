package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddCourseActivity extends AppCompatActivity {

    private TextInputEditText editTextClassName;


    private MaterialButton buttonSave;
    private FirestoreHandler firestoreHandler = new FirestoreHandler();

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        user = getIntent().getParcelableExtra("user");



        // Initialize views
        editTextClassName = findViewById(R.id.editTextClassName);
        buttonSave = findViewById(R.id.buttonSave);

        // Set click listener for save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClass();
            }
        });
    }

    private void saveClass() {
        String className = editTextClassName.getText().toString().trim();

        // Validate input
        if (className.isEmpty()) {
            editTextClassName.setError("Please enter a class name");
            return;
        }
        user.addCourse(className);

        firestoreHandler.updateUser(user);

        // Close the activity and return to previous screen
        Intent intent = new Intent();
        intent.putExtra("user",user);
        setResult(RESULT_OK,intent);
        finish();
    }
}