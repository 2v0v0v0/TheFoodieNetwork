package com.fbu.thefoodienetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fbu.thefoodienetwork.databinding.ActivityProfileBinding;
import com.fbu.thefoodienetwork.keys.UserKeys;
import com.parse.ParseFile;
import com.parse.ParseUser;

//TODO change LoginActivity goMainActivity when finish
public class ProfileActivity extends AppCompatActivity {
    private final static String TAG = "ProfileActivity";
    private ActivityProfileBinding binding;
    private ParseUser user = ParseUser.getCurrentUser();

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

        ParseFile image = user.getParseFile(UserKeys.profileImage);
        Log.i(TAG, image.getUrl());
        Glide.with(this).load(image.getUrl()).centerCrop().circleCrop().into(binding.profileImage);

        try {
            if (user.getString(UserKeys.screenName) == null || user.getString(UserKeys.screenName).trim().equals("")) {
                binding.textViewScreenName.setText(user.getUsername());
            } else {
                binding.textViewScreenName.setText(user.getString(UserKeys.screenName));
                binding.textViewUsername.setText(user.getUsername());
            }


            /*if (image != null) {
                Glide.with(this).load(image.getUrl()).centerCrop().circleCrop().into(binding.profileImage);
            } else {
                //Glide.with(this).load(R.drawable.placeholder).circleCrop().into(binding.profileImage);
            }*/
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e);
        }

    }

    private void goEditProfileActivity() {
        Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(editIntent);
    }

}