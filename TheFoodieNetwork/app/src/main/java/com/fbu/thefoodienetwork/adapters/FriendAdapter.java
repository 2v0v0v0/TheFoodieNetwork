package com.fbu.thefoodienetwork.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.R;
import com.parse.ParseUser;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private static final String TAG = "FriendAdapter";
    private Context context;
    private List<ParseUser> resultList;
    private List<String> friendList;
    private CurrentUserUtilities currentUserUtilities;

    public FriendAdapter(Context context, List<ParseUser> resultList, List<String> currentUserFriendList) {
        this.context = context;
        this.resultList = resultList;
        this.friendList = currentUserFriendList;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View friendView = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        return new FriendAdapter.ViewHolder(friendView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        ParseUser aUser = resultList.get(position);
        boolean setAddFriendButtonOn = false;
        if (!friendList.contains(aUser.getUsername())){
            setAddFriendButtonOn = true;
        }
        try {
            holder.bind(aUser, setAddFriendButtonOn);
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImageView;
        TextView screennameTextView;
        TextView usernameTextview;
        ImageView addFriednImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            screennameTextView = itemView.findViewById(R.id.screenNameTextView);
            usernameTextview = itemView.findViewById(R.id.usernameTextView);
            addFriednImageView = itemView.findViewById(R.id.addFriendImageView);
        }

        public void bind(ParseUser aUser, boolean setAddFriendButtonOn) throws Exception {
            //set add friend button to only users that not in current user's friend list
            if (setAddFriendButtonOn){
                addFriendButtonListener();
            }
            String screenName = aUser.get("screenName").toString();
            if(screenName != null){
                screennameTextView.setText(screenName);
            }
                usernameTextview.setText(aUser.getUsername());
        }

        private void addFriendButtonListener(){
            addFriednImageView.setVisibility(View.VISIBLE);
            addFriednImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    currentUserUtilities = new CurrentUserUtilities();
                    ParseUser otherUser = resultList.get(position);
                    Log.i(TAG, "onclick " + otherUser.getUsername());
                    boolean requestSuccess = currentUserUtilities.sendFriendRequest(otherUser);
                    Log.i(TAG, "success: "+ requestSuccess);
                    //addFriednImageView.setVisibility(View.GONE);
                }
            });
        }
    }
}
