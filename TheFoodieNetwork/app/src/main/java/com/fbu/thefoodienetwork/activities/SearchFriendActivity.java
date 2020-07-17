package com.fbu.thefoodienetwork.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fbu.thefoodienetwork.databinding.ActivityMainBinding;
import com.fbu.thefoodienetwork.databinding.ActivitySearchFriendBinding;

public class SearchFriendActivity extends AppCompatActivity {
    private final static String TAG = "SearchFriend";
    private ActivitySearchFriendBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchFriendBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}