package com.fbu.thefoodienetwork.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ActivityFriendListBinding;
import com.fbu.thefoodienetwork.fragments.FriendsFragment;
import com.fbu.thefoodienetwork.fragments.RequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FriendListActivity extends AppCompatActivity {
    private final static String TAG = "FriendList";
    private ActivityFriendListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.friendContainerFrameLayout, new FriendsFragment()).commit();

        binding.tabsNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_requests:
                        fragment = new RequestsFragment();
                        Log.i(TAG, "menu item: " + "requests");
                        break;
                    default:
                        fragment = new FriendsFragment();
                        Log.i(TAG, "menu item: " + "friends");
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.friendContainerFrameLayout, fragment).commit();
                return true;
            }
        });
        // Set default selection
        binding.tabsNavigation.setSelectedItemId(R.id.action_friends);
    }
}