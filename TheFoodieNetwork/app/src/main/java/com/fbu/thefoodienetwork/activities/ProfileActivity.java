package com.fbu.thefoodienetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.OnSwipeTouchListener;
import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.adapters.ReviewAdapter;
import com.fbu.thefoodienetwork.databinding.ActivityProfileBinding;
import com.fbu.thefoodienetwork.keys.ParcelKeys;
import com.fbu.thefoodienetwork.keys.UserKeys;
import com.fbu.thefoodienetwork.models.ParseReview;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

//TODO change LoginActivity goMainActivity when finish
public class ProfileActivity extends AppCompatActivity {
    private final static String TAG = "ProfileActivity";
    private ActivityProfileBinding binding;
    private ParseUser user;
    private boolean isCurrentUser = true;
    private ReviewAdapter reviewAdapter;
    private List<ParseReview> allReviews;
    private RecyclerView reviewRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        user = (ParseUser) Parcels.unwrap(getIntent().getParcelableExtra(ParcelKeys.SELECTED_USER));

        if(!CurrentUserUtilities.currentUser.getObjectId().equals(user.getObjectId())){
            isCurrentUser = false;
        }

        enableEditFunction(isCurrentUser);

        setSwipeListener(view);
        setContentView(view);

        reviewRecyclerView = binding.postsRecyclerView;
        allReviews = new ArrayList<>();

        reviewAdapter = new ReviewAdapter(this, allReviews);
        reviewRecyclerView.setAdapter(reviewAdapter);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reviewAdapter.clear();
        loadData();
    }


    private void setSwipeListener(View view){
        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                Toast.makeText(ProfileActivity.this, "Home", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }

    private void loadData() {
        queryReviews();

        try {

            if (user.getString(UserKeys.SCREEN_NAME) == null || user.getString(UserKeys.SCREEN_NAME).trim().equals("")) {
                binding.textViewScreenName.setText(user.getUsername());
            } else {
                binding.textViewScreenName.setText(user.getString(UserKeys.SCREEN_NAME));
                binding.textViewUsername.setText(user.getUsername());
            }


            try {
                binding.bioTextView.setText(user.getString(UserKeys.BIO));
            } catch (Exception e) {
                Log.i(TAG, "error:" + e.toString());
            }

            ParseFile image = user.getParseFile(UserKeys.PROFILE_IMAGE);
            if (image != null) {
                Glide.with(this).load(image.getUrl()).centerCrop().circleCrop().into(binding.profileImage);
            } else {
                Glide.with(this).load(R.drawable.placeholder).circleCrop().into(binding.profileImage);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error: " + e);
        }
    }

    private void enableEditFunction(boolean enable){
        if(!enable){
            return;
        }

        binding.editButton.setVisibility(View.VISIBLE);
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

    private void queryReviews() {
        ParseQuery<ParseReview> query = ParseQuery.getQuery(ParseReview.class);

        query.include(ParseReview.AUTHOR_KEY);
        query.include(ParseReview.RESTAURANT_KEY);

        if(!isCurrentUser && !CurrentUserUtilities.currentUserFriendList.contains(user.getObjectId())){
            query.whereEqualTo(ParseReview.GLOBAL_KEY, true);
        }

        query.whereEqualTo(ParseReview.AUTHOR_KEY, user);
        query.setLimit(10);
        query.addDescendingOrder(ParseReview.CREATED_AT_KEY);

        query.findInBackground(new FindCallback<ParseReview>() {
            @Override
            public void done(List<ParseReview> reviewList, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting reviews");
                    return;
                }

                for (ParseReview review : reviewList) {
                    Log.i(TAG, "Post: " + review.getText() + ", username: " + review.getAuthor().getUsername());
                }
                allReviews.addAll(reviewList);
                reviewAdapter.notifyDataSetChanged();
            }
        });

    }

}