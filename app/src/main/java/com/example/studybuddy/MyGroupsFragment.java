package com.example.studybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;
import com.example.studybuddy.DatabaseHelper;

public class MyGroupsFragment extends Fragment {
    private ListView groupsListView;
    private FloatingActionButton createGroupFab;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_groups, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        groupsListView = view.findViewById(R.id.groupsListView);
        createGroupFab = view.findViewById(R.id.createGroupFab);

        createGroupFab.setOnClickListener(v -> showCreateGroupDialog());

        // Load and display groups
        loadGroups();

        return view;
    }

    private void showCreateGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_group, null);

        EditText groupNameInput = dialogView.findViewById(R.id.groupNameInput);
        EditText courseInput = dialogView.findViewById(R.id.courseInput);

        builder.setView(dialogView)
                .setTitle("Create New Study Group")
                .setPositiveButton("Create", (dialog, which) -> {
                    String groupName = groupNameInput.getText().toString().trim();
                    String courseName = courseInput.getText().toString().trim();

                    if (!groupName.isEmpty() && !courseName.isEmpty()) {
                        createGroup(groupName, courseName);
                    } else {
                        Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void createGroup(String name, String course) {
        // Add group to database and refresh list
        long result = dbHelper.createGroup(name, course);
        if (result != -1) {
            Toast.makeText(getActivity(), "Group created successfully", Toast.LENGTH_SHORT).show();
            loadGroups();
        } else {
            Toast.makeText(getActivity(), "Failed to create group", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadGroups() {
        // Implement loading groups from database
        // Update groupsListView with data
    }
}