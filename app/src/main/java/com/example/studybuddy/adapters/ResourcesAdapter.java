package com.example.studybuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studybuddy.R;
import com.example.studybuddy.models.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.ViewHolder> {
    private final Context context;
    private List<Resource> resources;
    private final SimpleDateFormat dateFormat;

    public ResourcesAdapter(Context context, List<Resource> resources) {
        this.context = context;
        this.resources = resources;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resource resource = resources.get(position);

        holder.resourceNameText.setText(resource.getName());
        holder.uploaderText.setText("Uploaded by: " + resource.getUploaderName());

        if (resource.getUploadDate() != null) {
            holder.uploadTimeText.setText("Uploaded on: " +
                    dateFormat.format(resource.getUploadDate()));
        }

        holder.resourceTypeIcon.setImageResource(R.drawable.ic_file);

        holder.downloadButton.setOnClickListener(v -> {
            if (resource.getUrl() != null && !resource.getUrl().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(resource.getUrl()));
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context,
                            "Unable to open this file type", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context,
                        "Download link not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public void updateList(List<Resource> newList) {
        this.resources = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView resourceTypeIcon;
        final TextView resourceNameText;
        final TextView uploaderText;
        final TextView uploadTimeText;
        final ImageButton downloadButton;

        ViewHolder(View itemView) {
            super(itemView);
            resourceTypeIcon = itemView.findViewById(R.id.resourceTypeIcon);
            resourceNameText = itemView.findViewById(R.id.resourceNameText);
            uploaderText = itemView.findViewById(R.id.uploaderText);
            uploadTimeText = itemView.findViewById(R.id.uploadTimeText);
            downloadButton = itemView.findViewById(R.id.downloadButton);
        }
    }
}