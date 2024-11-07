package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FirstPageActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener{

    private User user;
    private RecyclerView recyclerViewCourses;
    private CourseAdapter adapter;
    private ArrayList<String> courses;

    private static final int ADD_COURSE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.first_page);

        user = getIntent().getParcelableExtra("user");

        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));

        // Initialize course list
        courses = new ArrayList<>();
        adapter = new CourseAdapter(courses, this);
        recyclerViewCourses.setAdapter(adapter);

        // Set up buttons
        Button buttonHomeScreen = findViewById(R.id.buttonHomeScreen);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        Button buttonNewCourse = findViewById(R.id.buttonAddCourse);

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
            startActivity(intent);
        });

        loadCourses();



    }
    private void loadCourses() {
        // This would typically come from your database or API
        courses = user.selectedCourses;
        System.out.println(courses);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCourseClick(String course) {
        // Navigate to course details or study groups for this course
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("course", course);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
            loadCourses();
        }
    }


}
