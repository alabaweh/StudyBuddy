package com.example.studybuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.studybuddy.R;
import com.example.studybuddy.models.User;
import java.util.List;

public class MembersAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> members;
    private boolean isAdmin;
    private String currentUserId;
    private OnMemberRemoveListener removeListener;

    public interface OnMemberRemoveListener {
        void onMemberRemove(User user);
    }

    public MembersAdapter(Context context, List<User> members, boolean isAdmin,
                          String currentUserId, OnMemberRemoveListener removeListener) {
        super(context, 0, members);
        this.context = context;
        this.members = members;
        this.isAdmin = isAdmin;
        this.currentUserId = currentUserId;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_member, parent, false);
        }

        User member = members.get(position);
        TextView memberNameText = convertView.findViewById(R.id.memberNameText);
        ImageView removeButton = convertView.findViewById(R.id.removeButton);

        memberNameText.setText(member.getName());

        // Show remove button only if current user is admin and member is not admin
        if (isAdmin && !member.getId().equals(members.get(0).getId()) &&
                !member.getId().equals(currentUserId)) {
            removeButton.setVisibility(View.VISIBLE);
            removeButton.setOnClickListener(v -> {
                if (removeListener != null) {
                    removeListener.onMemberRemove(member);
                }
            });
        } else {
            removeButton.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return members.size();
    }
}