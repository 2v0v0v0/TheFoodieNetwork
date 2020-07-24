package com.fbu.thefoodienetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.fbu.thefoodienetwork.databinding.ActivityProfileBinding;
import com.parse.ParseUser;

//TODO change LoginActivity goMainActivity when finish
public class ProfileActivity extends AppCompatActivity {
    private final static String TAG = "ProfileActivity";
    private ActivityProfileBinding binding;
    private ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEditProfileActivity();
            }
        });
    }

    private void goEditProfileActivity() {
        Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(editIntent);
    }

}