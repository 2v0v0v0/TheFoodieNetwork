package com.fbu.thefoodienetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.OnSwipeTouchListener;
import com.fbu.thefoodienetwork.adapters.FriendAdapter;
import com.fbu.thefoodienetwork.databinding.ActivitySearchFriendBinding;
import com.fbu.thefoodienetwork.keys.UserKeys;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendActivity extends AppCompatActivity {
    private final static String TAG = "SearchFriend";
    private final static String MODIFIER = "i";
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private List<ParseUser> resultList;
    private List<String> currentUserFriendList;
    private List<String> currentUserReceivedFriendRequest;
    private FriendAdapter friendAdapter;
    private ActivitySearchFriendBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchFriendBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSearchListener();
        setSwipeListener(binding.resultsRecyclerView);

        currentUserFriendList = CurrentUserUtilities.currentUserFriendList;
        currentUserReceivedFriendRequest = CurrentUserUtilities.currentUserReceivedFriendRequest;

        resultList = new ArrayList<>();
    }

    private void setSwipeListener(View view){
        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                //TODO swipe down to refresh
                Toast.makeText(SearchFriendActivity.this, "refresh", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                Toast.makeText(SearchFriendActivity.this, "Home", Toast.LENGTH_SHORT).show();
                goToMain();
            }

            @Override
            public void onSwipeRight() {
                Toast.makeText(SearchFriendActivity.this, "Search for Restaurant", Toast.LENGTH_SHORT).show();
                goToSearchRestaurant();
            }
        });
    }

    private void goToMain() {
        Intent mainIntent = new Intent(SearchFriendActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void goToSearchRestaurant() {
        Intent searchRestaurantIntent = new Intent(SearchFriendActivity.this, SearchActivity.class);
        startActivity(searchRestaurantIntent);
    }


    private void setSearchListener() {
        binding.searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                String keyword = binding.searchEditText.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !keyword.isEmpty()) {
                    Log.i(TAG, "search for: " + keyword);
                    resultList.clear();
                    searchForUsernameAndScreenName(keyword);
                    return true;
                }
                binding.searchEditText.setError("Cannot be empty");
                return false;
            }
        });
    }

    private void searchForUsernameAndScreenName(final String keyword) {
        ParseQuery<ParseUser> queryByUsername = ParseUser.getQuery();
        queryByUsername.whereMatches(UserKeys.USERNAME, keyword, MODIFIER);

        ParseQuery<ParseUser> queryByScreenName = ParseUser.getQuery();
        queryByScreenName.whereMatches(UserKeys.SCREEN_NAME, keyword, MODIFIER);

        List<ParseQuery<ParseUser>> queries = new ArrayList<>();
        queries.add(queryByUsername);
        queries.add(queryByScreenName);

        ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);
        mainQuery.whereNotEqualTo(UserKeys.OBJECT_ID, currentUser.getObjectId());
        mainQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> results, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting users");
                    return;
                }
                for (ParseUser user : results) {
                    Log.i(TAG, "result: " + user.getUsername() + " " + user.get(UserKeys.USERNAME));
                }
                resultList.addAll(results);
                binding.resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                friendAdapter = new FriendAdapter(SearchFriendActivity.this, resultList);
                binding.resultsRecyclerView.setAdapter(friendAdapter);
            }
        });
    }

}