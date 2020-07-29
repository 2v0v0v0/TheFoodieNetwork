package com.fbu.thefoodienetwork.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fbu.thefoodienetwork.databinding.ActivityBookmarkBinding;
import com.fbu.thefoodienetwork.databinding.ActivityEditProfileBinding;
import com.parse.ParseUser;

public class BookmarkActivity extends AppCompatActivity {
    private final static String TAG = "BookmarkActivity";
    private ActivityBookmarkBinding binding;
    private ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
    }
}