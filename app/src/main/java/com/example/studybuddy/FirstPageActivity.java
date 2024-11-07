package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FirstPageActivity extends AppCompatActivity{

    private User user;
    private RecyclerView recyclerViewCourses;
    private CourseAdapter adapter;
    private ArrayList<String> courses;

    private ActivityResultLauncher<Intent> intentLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.first_page);

        user = getIntent().getParcelableExtra("user");

        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));

        // Initialize course list
        courses = new ArrayList<String>();
        adapter = new CourseAdapter();
        recyclerViewCourses.setAdapter(adapter);

        // Set up buttons
        Button buttonHomeScreen = findViewById(R.id.buttonHomeScreen);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        Button buttonNewCourse = findViewById(R.id.buttonAddCourse);

        loadCourses();

        buttonHomeScreen.setOnClickListener(v -> {
            // Navigate to home screen
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            finish();
        });
        buttonLogout.setOnClickListener(v ->{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        });
        buttonNewCourse.setOnClickListener(v->{
            Intent intent = new Intent(this, AddCourseActivity.class);
            intent.putExtra("user",user);
            intentLauncher.launch(intent);
        });

        intentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(androidx.activity.result.ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            // Extract the data from the result Intent
                            user = result.getData().getParcelableExtra("user");
                            loadCourses();

                            // You can now use these values (for example, show them as a Toast)

                        }
                    }
                }
        );





    }
    private void loadCourses() {
        // This would typically come from your database or API
        List<String> newCourses = user.selectedCourses;
        adapter.submitList(newCourses);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Extract the modified data from the Intent
            user = data.getParcelableExtra("user");
            loadCourses();
    }
        }


}
