package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SessionsFragment extends Fragment {
    private CalendarView calendarView;
    private ListView sessionsListView;
    private FloatingActionButton createSessionButton;
    private ExtendedFloatingActionButton messageTeammatesButton;
    private TextView emptyView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sessions, container, false);

        // Initialize views
        calendarView = view.findViewById(R.id.calendarView);
        sessionsListView = view.findViewById(R.id.sessionsListView);
        createSessionButton = view.findViewById(R.id.createSessionButton);
        messageTeammatesButton = view.findViewById(R.id.messageTeammatesButton);
        emptyView = view.findViewById(R.id.emptyView);

        // Set empty view for listView
        sessionsListView.setEmptyView(emptyView);

        // Setup calendar listener
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            loadSessionsForDate(year, month, dayOfMonth);
        });

        // Setup create session button
        createSessionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateSessionActivity.class);
            startActivity(intent);
        });

        // Setup message teammates button
        messageTeammatesButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });

        // Load initial data
        loadCurrentDateSessions();

        return view;
    }

    private void loadSessionsForDate(int year, int month, int dayOfMonth) {
        // TODO: Load sessions for selected date from database
        // Format date
        String selectedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);

        // Query database for sessions on this date
        // Update sessionsListView with results
    }

    private void loadCurrentDateSessions() {
        // Load sessions for current date when fragment is created
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        loadSessionsForDate(
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload sessions when returning to this fragment
        loadCurrentDateSessions();
    }
}