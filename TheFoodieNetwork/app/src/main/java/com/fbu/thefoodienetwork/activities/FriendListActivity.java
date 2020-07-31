package com.fbu.thefoodienetwork.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fbu.thefoodienetwork.databinding.ActivityFriendListBinding;
import com.fbu.thefoodienetwork.databinding.ActivityMainBinding;

public class FriendListActivity extends AppCompatActivity {
    private final static String TAG = "FriendList";
    private ActivityFriendListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}