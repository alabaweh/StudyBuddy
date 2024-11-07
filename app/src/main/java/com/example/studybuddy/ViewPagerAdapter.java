// File: app/src/main/java/com/example/studybuddy/ViewPagerAdapter.java
package com.example.studybuddy;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyGroupsFragment();
            case 1:
                return new SessionsFragment();
            case 2:
                return new ResourcesFragment();
            default:
                return new MyGroupsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}