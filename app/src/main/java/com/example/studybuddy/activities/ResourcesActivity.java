package com.example.studybuddy.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import com.example.studybuddy.R;
import com.example.studybuddy.adapters.ResourcesAdapter;
import com.example.studybuddy.models.Resource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.*;

public class ResourcesActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST = 1;

    private ListView resourcesListView;
    private SearchView searchView;
    private TextView noResourcesTextView;
    private FloatingActionButton uploadButton;
    private ProgressBar progressBar;

    private ResourcesAdapter resourcesAdapter;
    private List<Resource> resources;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        groupId = getIntent().getStringExtra("groupId");
        if (groupId == null) {
            Toast.makeText(this, "Error: Group ID not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        initializeFirebase();
        setupListeners();
        loadResources();
    }

    private void initializeViews() {
        resourcesListView = findViewById(R.id.resourcesListView);
        searchView = findViewById(R.id.searchView);
        noResourcesTextView = findViewById(R.id.noResourcesTextView);
        uploadButton = findViewById(R.id.uploadButton);
        progressBar = findViewById(R.id.progressBar);

        resources = new ArrayList<>();
        resourcesAdapter = new ResourcesAdapter(this, resources);
        resourcesListView.setAdapter((ListAdapter) resourcesAdapter);
    }

    private void initializeFirebase() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    private void setupListeners() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterResources(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResources(newText);
                return true;
            }
        });

        uploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICK_FILE_REQUEST);
        });
    }

    private void loadResources() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("groups")
                .document(groupId)
                .collection("resources")
                .orderBy("uploadDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    progressBar.setVisibility(View.GONE);

                    if (error != null) {
                        Toast.makeText(this, "Error loading resources", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    resources.clear();
                    if (value != null) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Resource resource = doc.toObject(Resource.class);
                            if (resource != null) {
                                resource.setId(doc.getId());
                                resources.add(resource);
                            }
                        }
                    }

                    updateUI();
                });
    }

    private void filterResources(String query) {
        List<Resource> filteredList = new ArrayList<>();
        for (Resource resource : resources) {
            if (resource.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(resource);
            }
        }
        resourcesAdapter.updateList(filteredList);
        updateUI();
    }

    private void updateUI() {
        if (resources.isEmpty()) {
            noResourcesTextView.setVisibility(View.VISIBLE);
            resourcesListView.setVisibility(View.GONE);
        } else {
            noResourcesTextView.setVisibility(View.GONE);
            resourcesListView.setVisibility(View.VISIBLE);
            resourcesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                data != null &&
                data.getData() != null) {

            uploadFile(data.getData());
        }
    }

    private void uploadFile(Uri fileUri) {
        progressBar.setVisibility(View.VISIBLE);
        uploadButton.setEnabled(false);

        String fileName = System.currentTimeMillis() + "_" + fileUri.getLastPathSegment();
        StorageReference fileRef = storage.getReference()
                .child("groups")
                .child(groupId)
                .child("resources")
                .child(fileName);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Resource resource = new Resource();
                        resource.setName(fileName);
                        resource.setUrl(uri.toString());
                        resource.setUploaderId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        resource.setUploadDate(new Date());

                        saveResourceToFirestore(resource);
                    });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    uploadButton.setEnabled(true);
                    Toast.makeText(this, "Upload failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void saveResourceToFirestore(Resource resource) {
        db.collection("groups")
                .document(groupId)
                .collection("resources")
                .add(resource)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE);
                    uploadButton.setEnabled(true);
                    Toast.makeText(this, "Resource uploaded successfully",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    uploadButton.setEnabled(true);
                    Toast.makeText(this, "Failed to save resource details",
                            Toast.LENGTH_SHORT).show();
                });
    }
}