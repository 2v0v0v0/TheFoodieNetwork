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


import com.fbu.thefoodienetwork.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private static final String TAG = "FriendAdapter";
    private Context context;
    private List<ParseUser> userList;

    public FriendAdapter(Context context, List<ParseUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View friendView = LayoutInflater.from(context).inflate(R.layout.item_firend, parent, false);
        return new FriendAdapter.ViewHolder(friendView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        ParseUser aUser = userList.get(position);
        try {
            holder.bind(aUser);
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImageView;
        TextView screennameTextView;
        TextView usernameTextview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            screennameTextView = itemView.findViewById(R.id.screenNameTextView);
            usernameTextview = itemView.findViewById(R.id.usernameTextView);
        }

        public void bind(ParseUser aUser) throws Exception {
            String screenName = aUser.get("screenName").toString();
            if(screenName != null){
                screennameTextView.setText(screenName);
            }
                usernameTextview.setText(aUser.getUsername());
        }
    }
}
