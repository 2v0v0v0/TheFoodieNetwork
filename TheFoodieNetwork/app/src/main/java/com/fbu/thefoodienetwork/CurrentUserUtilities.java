package com.fbu.thefoodienetwork;

import android.util.Log;

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
        //Create new friend request and save to database
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

        //update currentUser local data
        currentUserSentFriendRequest.add(otherUser.getObjectId());
        return true;
    }

    public static boolean acceptFriendRequest(ParseUser otherUser) {
        //Add otherUser to currentUser friends relation
        ParseRelation<ParseUser> relation = currentUser.getRelation("friends");
        relation.add(otherUser);
        try {
            currentUser.save();
        } catch (ParseException e) {
            if (e != null) {
                Log.i("acceptFriendRequest", e.toString());
                return false;
            }
        }

        //Delete friend request from FriendRequest class
        ParseObject result;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        query.whereEqualTo("from", otherUser);
        query.whereEqualTo("to", currentUser);
        try {
            result = query.getFirst();
            result.delete();
            result.saveInBackground();
            Log.i("acceptFriendRequest", result.getObjectId());
        } catch (ParseException e) {
            if (e != null) {
                Log.i("acceptFriendRequest", e.toString());
                return false;
            }
        }

        //update currentUser local data
        currentUserReceivedFriendRequest.remove(otherUser.getObjectId());
        currentUserFriendList.add(otherUser.getObjectId());
        return true;
    }

    public static boolean deleteFriendRequest(ParseUser otherUser) {
        //Set isDeclined field in friend request object as true
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

        //update currentUser local data
        currentUserReceivedFriendRequest.remove(otherUser.getObjectId());
        return true;
    }

    private static void getFriendList() {
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

    private static void getPendingFriendRequest() {
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

    private static void getSentFriendRequest() {
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
