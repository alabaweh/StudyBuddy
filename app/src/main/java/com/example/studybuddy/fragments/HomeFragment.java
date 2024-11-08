package com.example.studybuddy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studybuddy.R;
import com.example.studybuddy.activities.GroupDetailsActivity;
import com.example.studybuddy.adapters.GroupsAdapter;
import com.example.studybuddy.models.Group;
import com.example.studybuddy.models.User;
import com.example.studybuddy.utils.FirebaseUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements GroupsAdapter.OnGroupClickListener {
    private RecyclerView groupsRecyclerView;
    private TextView noGroupsTextView;
    private TextView welcomeTextView;
    private FloatingActionButton addGroupButton;
    private ProgressBar progressBar;

    private GroupsAdapter groupsAdapter;
    private List<Group> groups;
    private FirebaseFirestore db;
    private User currentUser;
    private String currentUserId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(view);
        initializeFirebase();
        setupListeners();

        return view;
    }

    private void initializeViews(View view) {
        groupsRecyclerView = view.findViewById(R.id.groupsRecyclerView);
        noGroupsTextView = view.findViewById(R.id.noGroupsTextView);
        welcomeTextView = view.findViewById(R.id.welcomeTextView);
        addGroupButton = view.findViewById(R.id.addGroupButton);
        progressBar = view.findViewById(R.id.progressBar);

        groups = new ArrayList<>();
        groupsAdapter = new GroupsAdapter(requireContext(), groups, this);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        groupsRecyclerView.setAdapter((RecyclerView.Adapter) groupsAdapter);

        noGroupsTextView.setText("No groups available yet");
    }

    private void initializeFirebase() {
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseUtils.getCurrentUser()
                .addOnSuccessListener(user -> {
                    currentUser = user;
                    if (user != null) {
                        welcomeTextView.setText("Welcome, " + user.getName());
                        loadAllGroups();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(),
                            "Failed to get current user", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void setupListeners() {
        addGroupButton.setOnClickListener(v -> {
            CreateGroupDialogFragment dialog = new CreateGroupDialogFragment();
            dialog.setOnGroupCreatedListener(this::loadAllGroups);
            dialog.show(getParentFragmentManager(), "CreateGroupDialog");
        });
    }

    private void loadAllGroups() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("groups")
                .addSnapshotListener((value, error) -> {
                    progressBar.setVisibility(View.GONE);

                    if (error != null) {
                        Toast.makeText(getContext(),
                                "Error loading groups", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    groups.clear();
                    if (value != null) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Group group = doc.toObject(Group.class);
                            if (group != null) {
                                group.setId(doc.getId());
                                groups.add(group);
                            }
                        }
                    }

                    updateUI();
                });
    }

    private void updateUI() {
        if (groups.isEmpty()) {
            noGroupsTextView.setVisibility(View.VISIBLE);
            groupsRecyclerView.setVisibility(View.GONE);
        } else {
            noGroupsTextView.setVisibility(View.GONE);
            groupsRecyclerView.setVisibility(View.VISIBLE);
            groupsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGroupClick(Group group) {
        Intent intent = new Intent(getActivity(), GroupDetailsActivity.class);
        intent.putExtra("groupId", group.getId());
        startActivity(intent);
    }
}