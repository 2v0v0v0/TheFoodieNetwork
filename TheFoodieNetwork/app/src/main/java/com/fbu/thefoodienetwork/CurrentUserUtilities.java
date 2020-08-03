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
    private static CurrentUserUtilities instance;

    private List<String> currentUserFriendList;
    private List<String> currentUserReceivedFriendRequest;
    private List<String> currentUserSentFriendRequest;

    private List<ParseUser> friendParseUserList;
    private List<ParseUser> requestParseUserList;
    private List<ParseUser> pendingParseUserList;
    private ParseUser currentUser;

    private CurrentUserUtilities() {
        currentUser = ParseUser.getCurrentUser();

        currentUserFriendList = new ArrayList<>();
        currentUserReceivedFriendRequest = new ArrayList<>();
        currentUserSentFriendRequest = new ArrayList<>();

        requestParseUserList = new ArrayList<>();
        friendParseUserList = new ArrayList<>();
        pendingParseUserList = new ArrayList<>();

        BookmarkActivity.queryBookmarks();

        fetchFriendList();
        fetchPendingFriendRequest();
        fetchSentFriendRequest();
    }

    public static CurrentUserUtilities getInstance(){
        if(instance == null){
            instance = new CurrentUserUtilities();
        }

        return instance;
    }

    public void cleanUp(){
        instance = null;
        currentUser = null;

        currentUserFriendList = null;
        currentUserReceivedFriendRequest = null;
        currentUserSentFriendRequest = null;

        requestParseUserList = null;
        friendParseUserList = null;
        pendingParseUserList = null;
    }


    //Actions
    public boolean sendFriendRequest(ParseUser otherUser) {
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

    public boolean cancelFriendRequest(ParseUser otherUser) {
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
        pendingParseUserList.remove(otherUser);

        return true;
    }

    public boolean acceptFriendRequest(ParseUser otherUser) {
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

    public boolean deleteFriendRequest(ParseUser otherUser) {
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
        requestParseUserList.remove(otherUser);
        return true;
    }


    //Fetch data
    private void fetchFriendList() {
        ParseRelation relation = currentUser.getRelation(UserKeys.FRIENDS);
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> results, ParseException e) {

                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }

                for (ParseUser user : results) {
                    currentUserFriendList.add(user.getObjectId());
                }

                friendParseUserList.addAll(results);

            }
        });
    }

    private void fetchPendingFriendRequest() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(FriendRequestKeys.PARSE_KEY);

        query.whereEqualTo(FriendRequestKeys.TO, currentUser);
        query.whereEqualTo(FriendRequestKeys.IS_DECLINED, false);
        query.include(FriendRequestKeys.FROM);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> requests, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }
                for (ParseObject friendRequest : requests) {
                    ParseUser user = friendRequest.getParseUser(FriendRequestKeys.FROM);
                    String userID = user.getObjectId();
                    requestParseUserList.add(user);
                    currentUserReceivedFriendRequest.add(userID);
                    Log.i(TAG, "received: " + userID);
                }

                Log.i(TAG, "list: " + requestParseUserList.toString());
            }
        });
    }

    private void fetchSentFriendRequest() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(FriendRequestKeys.PARSE_KEY);

        query.whereEqualTo(FriendRequestKeys.FROM, currentUser);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }
                for (ParseObject object : results) {
                    try {
                        ParseUser user = object.fetchIfNeeded().getParseUser(FriendRequestKeys.TO);
                        String userID = user.fetchIfNeeded().getObjectId();
                        currentUserSentFriendRequest.add(userID);
                        pendingParseUserList.add(user);
                        Log.i(TAG, "sent: " + userID);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
    }


    //getters

    public List<String> getCurrentUserFriendList() {
        return currentUserFriendList;
    }

    public List<String> getCurrentUserReceivedFriendRequest() {
        return currentUserReceivedFriendRequest;
    }

    public List<String> getCurrentUserSentFriendRequest() {
        return currentUserSentFriendRequest;
    }

    public List<ParseUser> getFriendParseUserList() {
        return friendParseUserList;
    }

    public List<ParseUser> getRequestParseUserList() {
        return requestParseUserList;
    }

    public List<ParseUser> getPendingParseUserList() {
        return pendingParseUserList;
    }

    public ParseUser getCurrentUser() {
        return currentUser;
    }
}
