package com.fbu.thefoodienetwork.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.adapters.FriendAdapter;
import com.fbu.thefoodienetwork.databinding.ActivityFriendListBinding;
import com.fbu.thefoodienetwork.databinding.ActivityMainBinding;
import com.fbu.thefoodienetwork.keys.UserKeys;
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

        queryFriend();
    }

    private void queryFriend() {
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
    }
}