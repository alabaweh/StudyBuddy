package com.example.studybuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {
    private ListView messagesListView;
    private EditText messageInput;
    private ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesListView = findViewById(R.id.messagesListView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageInput.setText("");
            }
        });

        // Load existing messages
        loadMessages();
    }

    private void sendMessage(String message) {
        // Implement message sending functionality
        // Add message to database and update UI
    }

    private void loadMessages() {
        // Implement loading messages from database
        // Update messagesListView with data
    }
}