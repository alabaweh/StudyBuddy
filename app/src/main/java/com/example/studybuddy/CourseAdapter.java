package com.example.studybuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private List<String> courses;
    private OnCourseClickListener listener;

    public interface OnCourseClickListener {
        void onCourseClick(String course);
    }

    public CourseAdapter(List<String> courses, OnCourseClickListener listener) {
        this.courses = courses;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String course = courses.get(position);
        holder.textViewCourseName.setText(course);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCourseClick(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCourseName;

        public ViewHolder(View view) {
            super(view);
            textViewCourseName = view.findViewById(R.id.textViewCourseName);
        }
    }
}
