package com.fbu.thefoodienetwork.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ActivityFriendListBinding;
import com.fbu.thefoodienetwork.fragments.FriendsFragment;
import com.fbu.thefoodienetwork.fragments.PendingFragment;
import com.fbu.thefoodienetwork.fragments.RequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FriendListActivity extends AppCompatActivity {
    private final static String TAG = "FriendList";
    private final static String FRIENDS_FRAG_TAG = "FriendsFragment";
    private final static String PENDING_FRAG_TAG = "PendingFragment";
    private final static String REQUESTS_FRAG_TAG = "RequestsFragment";

    private final Fragment friendsFragment = new FriendsFragment();
    private final Fragment pendingFragment = new PendingFragment();
    private final Fragment requestsFragment = new RequestsFragment();
    final private FragmentManager fragmentManager = getSupportFragmentManager();

    private ActivityFriendListBinding binding;

    private Fragment active = friendsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fragmentManager.beginTransaction().add(R.id.friendContainerFrameLayout, requestsFragment, REQUESTS_FRAG_TAG).hide(requestsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.friendContainerFrameLayout, pendingFragment, PENDING_FRAG_TAG).hide(pendingFragment).commit();
        fragmentManager.beginTransaction().add(R.id.friendContainerFrameLayout, friendsFragment, FRIENDS_FRAG_TAG).commit();


        binding.tabsNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_requests:
                        fragmentManager.beginTransaction().hide(active).show(requestsFragment).commit();
                        active = requestsFragment;
                        return true;

                    case R.id.action_pending:
                        fragmentManager.beginTransaction().hide(active).show(pendingFragment).commit();
                        active = pendingFragment;
                        return true;
                    case R.id.action_friends:
                        fragmentManager.beginTransaction().hide(active).show(friendsFragment).commit();
                        active = friendsFragment;
                        return true;
                }

                return false;
            }
        });
        // Set default selection
        binding.tabsNavigation.setSelectedItemId(R.id.action_friends);
    }
}