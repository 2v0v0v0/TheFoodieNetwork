package com.fbu.thefoodienetwork;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CurrentUserUtilities {
    private final static String TAG = "CurrentUserUtilities";
    public static List<String> currentUserFriendList;
    public static List<String> currentUserPendingFriendRequest;
    public static List<String> currentUserSentFriendRequest;
    private static ParseUser currentUser;

    public CurrentUserUtilities() {
        currentUser = ParseUser.getCurrentUser();
        currentUserFriendList = new ArrayList<>();
        currentUserPendingFriendRequest = new ArrayList<>();
        getFriendList();
        getPendingFriendRequest();
        getSentFriendRequest();
    }

    public static boolean sendFriendRequest(ParseUser otherUser) {
        //Create friend request
        ParseObject request = new ParseObject("FriendRequest");
        request.put("from", currentUser);
        request.put("to", otherUser);
        try {
            request.save();
        } catch (ParseException e) {
            if (e != null) {
                return false;
            }
        }
        return true;
    }

    private void getFriendList() {
        ParseRelation relation = currentUser.getRelation("friends");
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> results, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                } else {
                    for (ParseUser user : results) {
                        currentUserFriendList.add(user.getUsername());
                    }
                }
            }
        });
    }

    private void getPendingFriendRequest() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        query.whereEqualTo("to", currentUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }
                for (ParseObject object : objects) {
                    try {
                        ParseUser user = object.fetchIfNeeded().getParseUser("from");
                        String username = user.fetchIfNeeded().getUsername();
                        currentUserPendingFriendRequest.add(username);
                        Log.i(TAG, "received: " + username);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void getSentFriendRequest() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        query.whereEqualTo("from", currentUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }
                for (ParseObject object : objects) {
                    try {
                        ParseUser user = object.fetchIfNeeded().getParseUser("to");
                        String username = user.fetchIfNeeded().getUsername();
                        currentUserPendingFriendRequest.add(username);
                        Log.i(TAG, "sent: " + username);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
