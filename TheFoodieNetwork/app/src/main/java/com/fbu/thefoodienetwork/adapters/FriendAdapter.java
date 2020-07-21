package com.fbu.thefoodienetwork.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ItemFriendBinding;
import com.fbu.thefoodienetwork.databinding.ItemReviewBinding;
import com.parse.ParseUser;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    public static final int IS_STRANGER = 1;
    public static final int SENT_FR = -1;
    public static final int RECEIVED_FR = 2;
    private static final String TAG = "FriendAdapter";
    private static final int IS_FRIEND = 0;
    private Context context;
    private List<ParseUser> resultList;
    private List<String> friendList;
    private List<String> sentFRList;
    private List<String> receivedFRList;


    public FriendAdapter(Context context, List<ParseUser> resultList) {
        this.context = context;
        this.resultList = resultList;
        this.friendList = CurrentUserUtilities.currentUserFriendList;
        this.sentFRList = CurrentUserUtilities.currentUserSentFriendRequest;
        this.receivedFRList = CurrentUserUtilities.currentUserReceivedFriendRequest;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFriendBinding binding = ItemFriendBinding.inflate(layoutInflater, parent, false);
        return new FriendAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        ParseUser aUser = resultList.get(position);
        String userID = aUser.getObjectId();
        int relationStatus = IS_STRANGER;
        if (friendList.contains(userID)) {
            relationStatus = IS_FRIEND;
        } else if (sentFRList.contains(userID)) {
            relationStatus = SENT_FR;
        } else if (receivedFRList.contains(userID)){
            relationStatus = RECEIVED_FR;
        }
        Log.i("onBindViewHolder", aUser.getUsername() + " " + relationStatus);
        try {
            holder.bind(aUser, relationStatus);
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImageView;
        private TextView screennameTextView;
        private TextView usernameTextview;
        private ImageView addFriendImageView;
        private TextView pendingTextView;
        private LinearLayout receivedFRButtonsContainer;
        private final ItemFriendBinding binding;

        public ViewHolder(ItemFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            profileImageView = binding.profileImageView;
            screennameTextView = binding.screenNameTextView;
            usernameTextview = binding.usernameTextView;
            addFriendImageView = binding.addFriendImageView;
            pendingTextView = binding.pendingTextView;
            receivedFRButtonsContainer = binding.receivedFRButtonsContainer;
        }

        public void bind(ParseUser aUser, int relationStatus) throws Exception {
            //set add friend button to only users that not in current user's friend list
            Log.i("bind", aUser.getUsername() + " " + relationStatus);
            switch (relationStatus){
                case IS_STRANGER:
                    addFriendButtonListener();
                    break;
                case SENT_FR:
                    pendingTextView.setVisibility(View.VISIBLE);
                    break;
                case RECEIVED_FR:
                    receivedFRButtonsContainer.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }

            String screenName = aUser.get("screenName").toString();
            if (screenName != null) {
                screennameTextView.setText(screenName);
            }
            usernameTextview.setText(aUser.getUsername());
        }

        private void addFriendButtonListener() {
            addFriendImageView.setVisibility(View.VISIBLE);
            addFriendImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ParseUser otherUser = resultList.get(position);
                    Log.i(TAG, "onclick " + otherUser.getUsername());
                    boolean requestSuccess = CurrentUserUtilities.sendFriendRequest(otherUser);
                    Log.i(TAG, "success: " + requestSuccess);
                    //addFriednImageView.setVisibility(View.GONE);
                }
            });
        }
    }
}
