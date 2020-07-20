package com.fbu.thefoodienetwork;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fbu.thefoodienetwork.activities.SearchFriendActivity;
import com.fbu.thefoodienetwork.adapters.FriendAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CurrentUserUtilities {
    private final static String TAG = "CurrentUserUtilities";
    private final static ParseUser CURRENT_USER = ParseUser.getCurrentUser();
    public List<String> currentUserFriendList;
    public List<String> currentUserPendingFriendRequest;

    public CurrentUserUtilities() {
        currentUserFriendList = new ArrayList<>();
        currentUserPendingFriendRequest = new ArrayList<>();
    }

    public void getFriendList() {
        ParseRelation relation = CURRENT_USER.getRelation("friends");
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> results, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                } else {
                    for(ParseUser user : results){
                        currentUserFriendList.add(user.getUsername());
                    }
                }
            }
        });
    }

    public boolean sendFriendRequest(ParseUser otherUser){
        //Create friend request
        ParseObject request = new ParseObject("FriendRequest");
        request.put("from", CURRENT_USER);
        request.put("to", otherUser);
        try {
            request.save();
        } catch (ParseException e) {
            if(e != null){
                return false;
            }
        }
        return true;
    }

    public void getFriendRequestList(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        query.whereEqualTo("from", CURRENT_USER);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }
                for (ParseObject object : objects){
                    try {
                        ParseUser user = object.fetchIfNeeded().getParseUser("to");
                        String username  = user.fetchIfNeeded().getUsername();
                        currentUserPendingFriendRequest.add(username);
                        Log.i(TAG, username);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }


}
