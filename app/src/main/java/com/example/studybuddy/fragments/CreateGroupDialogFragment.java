package com.example.studybuddy.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.studybuddy.R;
import com.example.studybuddy.models.Group;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CreateGroupDialogFragment extends DialogFragment {
    private TextInputEditText groupNameInput;
    private AutoCompleteTextView courseSpinner;
    private FirebaseFirestore db;
    private String currentUserId;
    private Context context;
    private OnGroupCreatedListener listener;

    public interface OnGroupCreatedListener {
        void onGroupCreated();
    }

    public void setOnGroupCreatedListener(OnGroupCreatedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_group, null);

        initializeViews(view);
        setupCourseSpinner();

        builder.setView(view)
                .setTitle("Create Group")
                .setPositiveButton("Create", (dialog, id) -> createGroup())
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    private void initializeViews(View view) {
        groupNameInput = view.findViewById(R.id.groupNameInput);
        courseSpinner = view.findViewById(R.id.courseSpinner);
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void setupCourseSpinner() {
        List<String> courses = Arrays.asList(
                "Computer Science",
                "Mathematics",
                "Physics",
                "Chemistry",
                "Biology",
                "Engineering"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                courses
        );
        courseSpinner.setAdapter(adapter);
    }

    private void createGroup() {
        String groupName = groupNameInput.getText().toString().trim();
        String courseName = courseSpinner.getText().toString().trim();

        if (groupName.isEmpty() || courseName.isEmpty()) {
            showToast("Please fill all fields");
            return;
        }

        // Create group document
        Group group = new Group();
        group.setName(groupName);
        group.setCourseName(courseName);
        group.setCreatedBy(currentUserId);
        group.setCreatedAt(new Date());

        List<String> members = new ArrayList<>();
        members.add(currentUserId);
        group.setMembers(members);

        db.collection("groups")
                .add(group)
                .addOnSuccessListener(documentReference -> {
                    String groupId = documentReference.getId();
                    documentReference.update("id", groupId)
                            .addOnSuccessListener(aVoid -> {
                                if (context != null) {
                                    showToast("Group created successfully");
                                }
                                if (listener != null) {
                                    listener.onGroupCreated();
                                }
                            })
                            .addOnFailureListener(e -> {
                                if (context != null) {
                                    showToast("Failed to update group ID");
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    if (context != null) {
                        showToast("Failed to create group: " + e.getMessage());
                    }
                });
    }

    private void showToast(String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }
}