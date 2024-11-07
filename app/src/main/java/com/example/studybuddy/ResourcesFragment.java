package com.example.studybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;

public class ResourcesFragment extends Fragment {
    private SearchView searchView;
    private ListView resourcesListView;
    private FloatingActionButton uploadButton;
    private TextView emptyView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resources, container, false);

        // Initialize views
        searchView = view.findViewById(R.id.searchView);
        resourcesListView = view.findViewById(R.id.resourcesListView);
        uploadButton = view.findViewById(R.id.uploadButton);
        emptyView = view.findViewById(R.id.emptyView);

        // Set empty view for listView
        resourcesListView.setEmptyView(emptyView);

        // Setup search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchResources(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResources(newText);
                return true;
            }
        });

        // Setup upload button
        uploadButton.setOnClickListener(v -> showUploadDialog());

        return view;
    }

    private void searchResources(String query) {
        // TODO: Implement search functionality
        if (query != null && !query.isEmpty()) {
            // Filter resources based on query
            // Update resourcesListView with filtered results
        } else {
            // Show all resources when query is empty
            loadAllResources();
        }
    }

    private void showUploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Upload Resource")
                .setMessage("Select a file to upload")
                .setPositiveButton("Choose File", (dialog, which) -> {
                    // TODO: Implement file chooser
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadAllResources() {
        // TODO: Load all resources from database
    }
}