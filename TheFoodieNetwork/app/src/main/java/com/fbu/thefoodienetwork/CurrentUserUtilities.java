package com.fbu.thefoodienetwork;

import android.util.Log;

import com.fbu.thefoodienetwork.activities.BookmarkActivity;
import com.fbu.thefoodienetwork.keys.FriendRequestKeys;
import com.fbu.thefoodienetwork.keys.UserKeys;
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


    //Actions
    public static boolean sendFriendRequest(ParseUser otherUser) {
        //Create new friend request and save to database
        ParseObject request = new ParseObject(FriendRequestKeys.PARSE_KEY);
        request.put(FriendRequestKeys.FROM, currentUser);
        request.put(FriendRequestKeys.TO, otherUser);
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

    public static boolean cancelFriendRequest(ParseUser otherUser) {
        //Delete friend request from currentUser to otherUser from FriendRequest class
        ParseObject result;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(FriendRequestKeys.PARSE_KEY);
        query.whereEqualTo(FriendRequestKeys.FROM, currentUser);
        query.whereEqualTo(FriendRequestKeys.TO, otherUser);

        try {
            result = query.getFirst();
            result.delete();
            result.saveInBackground();
            Log.i("cancelFriendRequest", result.getObjectId());
        } catch (ParseException e) {
            if (e != null) {
                Log.i("cancelFriendRequest", e.toString());
                return false;
            }
        }

        //update currentUser local data
        currentUserSentFriendRequest.remove(otherUser.getObjectId());
        return true;
    }

    public static boolean acceptFriendRequest(ParseUser otherUser) {
        //Add otherUser to currentUser friends relation
        ParseRelation<ParseUser> relation = currentUser.getRelation(UserKeys.FRIENDS);
        relation.add(otherUser);
        try {
            currentUser.save();
        } catch (ParseException e) {
            if (e != null) {
                Log.i("acceptFriendRequest", e.toString());
                return false;
            }
        }

        //Delete friend request from otherUser to currentUser from FriendRequest class
        ParseObject result;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(FriendRequestKeys.PARSE_KEY);
        query.whereEqualTo(FriendRequestKeys.FROM, otherUser);
        query.whereEqualTo(FriendRequestKeys.TO, currentUser);
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery(FriendRequestKeys.PARSE_KEY);

        query.whereEqualTo(FriendRequestKeys.FROM, otherUser);
        query.whereEqualTo(FriendRequestKeys.TO, currentUser);

        try {
            result = query.getFirst();
            result.put(FriendRequestKeys.IS_DECLINED, true);
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


    //Data
    private static void getFriendList() {
        ParseRelation relation = currentUser.getRelation(UserKeys.FRIENDS);
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery(FriendRequestKeys.PARSE_KEY);

        query.whereEqualTo(FriendRequestKeys.TO, currentUser);
        query.whereEqualTo(FriendRequestKeys.IS_DECLINED, false);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> requests, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }
                for (ParseObject friendRequest : requests) {
                    try {

                        ParseUser user = friendRequest.fetchIfNeeded().getParseUser(FriendRequestKeys.FROM);
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery(FriendRequestKeys.PARSE_KEY);

        query.whereEqualTo(FriendRequestKeys.FROM, currentUser);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }
                for (ParseObject object : objects) {
                    try {
                        ParseUser user = object.fetchIfNeeded().getParseUser(FriendRequestKeys.TO);
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
