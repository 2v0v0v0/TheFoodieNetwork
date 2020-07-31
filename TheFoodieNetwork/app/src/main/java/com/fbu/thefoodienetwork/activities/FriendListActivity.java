package com.fbu.thefoodienetwork.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.adapters.FriendAdapter;
import com.fbu.thefoodienetwork.databinding.ActivityFriendListBinding;
import com.fbu.thefoodienetwork.databinding.ActivityMainBinding;
import com.fbu.thefoodienetwork.fragments.ComposeFragment;
import com.fbu.thefoodienetwork.fragments.FriendsFragment;
import com.fbu.thefoodienetwork.fragments.GlobeFragment;
import com.fbu.thefoodienetwork.fragments.HomeFragment;
import com.fbu.thefoodienetwork.keys.UserKeys;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {
    private final static String TAG = "FriendList";
    private ActivityFriendListBinding binding;
    private FriendAdapter friendAdapter;
    private List<ParseUser> friendList = new ArrayList<>();

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
                        fragment = new HomeFragment();
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
        binding.tabsNavigation.setSelectedItemId(R.id.action_home);
    }

    /*private void queryFriend() {
        ParseRelation relation = CurrentUserUtilities.currentUser.getRelation(UserKeys.FRIENDS);
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> results, ParseException e) {

                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }

                friendList.addAll(results);
                Log.i(TAG, friendList.toString());

                binding.friendRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                friendAdapter = new FriendAdapter(FriendListActivity.this, friendList);
                binding.friendRecyclerView.setAdapter(friendAdapter);
            }
        });
    }*/
}