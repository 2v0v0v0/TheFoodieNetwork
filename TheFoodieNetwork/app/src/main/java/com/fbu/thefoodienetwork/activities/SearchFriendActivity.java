package com.fbu.thefoodienetwork.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fbu.thefoodienetwork.databinding.ActivitySearchFriendBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendActivity extends AppCompatActivity {
    private final static String TAG = "SearchFriend";
    private final static String modifier = "i";
    private final static String USERNAME_KEY = "username";
    private final static String SCREENNAME_KEY = "screenName";
    private final ParseUser CURRENT_USER = ParseUser.getCurrentUser();
    List<ParseUser> userList;
    private ActivitySearchFriendBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchFriendBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        searchListener();
        userList = new ArrayList<>();
    }

    private void searchListener() {
        binding.searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                String keyword = binding.searchEditText.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !keyword.isEmpty()) {
                    Log.i(TAG, "search for: " + keyword);
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
        queryByUsername.whereMatches(USERNAME_KEY, keyword, modifier);

        ParseQuery<ParseUser> queryByScreenName = ParseUser.getQuery();
        queryByScreenName.whereMatches(SCREENNAME_KEY, keyword, modifier);

        List<ParseQuery<ParseUser>> queries = new ArrayList<>();
        queries.add(queryByUsername);
        queries.add(queryByScreenName);

        ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);
        mainQuery.whereNotEqualTo(USERNAME_KEY, CURRENT_USER.getUsername());
        mainQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> results, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting users");
                    return;
                }
                for (ParseUser user : results) {
                    Log.i(TAG, "result: " + user.getUsername() + " " + user.get(SCREENNAME_KEY));
                }
                userList.addAll(results);
            }
        });
    }


}