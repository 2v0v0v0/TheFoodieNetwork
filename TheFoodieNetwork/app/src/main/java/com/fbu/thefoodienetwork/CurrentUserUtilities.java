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
    public static List<String> currentUserReceivedFriendRequest;
    public static List<String> currentUserSentFriendRequest;
    private static ParseUser currentUser;

    public CurrentUserUtilities() {
        currentUser = ParseUser.getCurrentUser();
        currentUserFriendList = new ArrayList<>();
        currentUserReceivedFriendRequest = new ArrayList<>();
        currentUserSentFriendRequest = new ArrayList<>();
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

    public static boolean acceptFriendRequest(ParseUser otherUser) {
        return true;
    }

    public static boolean deleteFriendRequest(ParseUser otherUser) {
        ParseObject result = new ParseObject("FriendRequest");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        query.whereEqualTo("from", otherUser);
        query.whereEqualTo("to", currentUser);
        try {
            result = query.getFirst();
            result.put("isDeclined", true);
            result.saveInBackground();
            Log.i("deleteFriendRequest", result.getObjectId());
        } catch (ParseException e) {
            if (e != null) {
                return false;
            }
        }
        return true;
    }

    public static void getFriendList() {
        ParseRelation relation = currentUser.getRelation("friends");
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> results, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                } else {
                    for (ParseUser user : results) {
                        currentUserFriendList.add(user.getObjectId());
                    }
                }
            }
        });
    }

    public static void getPendingFriendRequest() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        query.whereEqualTo("to", currentUser);
        query.whereEqualTo("isDeclined", false);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> requests, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }
                for (ParseObject friendRequest : requests) {
                    try {
                        ParseUser user = friendRequest.fetchIfNeeded().getParseUser("from");
                        String userID = user.fetchIfNeeded().getObjectId();
                        currentUserReceivedFriendRequest.add(userID);
                        Log.i(TAG, "received: " + userID);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public static void getSentFriendRequest() {
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
                        String userID = user.fetchIfNeeded().getObjectId();
                        currentUserSentFriendRequest.add(userID);
                        Log.i(TAG, "sent: " + userID);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

}
