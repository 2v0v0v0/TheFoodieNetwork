package com.fbu.thefoodienetwork.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.databinding.ItemFriendBinding;
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
        } else if (receivedFRList.contains(userID)) {
            relationStatus = RECEIVED_FR;
        }

        try {
            holder.bind(aUser, relationStatus);
        } catch (Exception e) {
            Log.i(TAG, "error: " + e);
        }
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final int DELETE_FR_CODE = 1;
        private static final int ACCEPT_FR_CODE = 2;
        private static final int SEND_FR_CODE = 3;
        private static final int CANCEL_FR_CODE = 4;
        private int position;
        private ParseUser otherUser;
        private String otherUserId;
        private String otherUserUsername;

        private ImageView profileImageView;
        private TextView screennameTextView;
        private TextView usernameTextView;
        private ImageView addFriendImageView;
        private TextView pendingTextView;
        private Button acceptFR;
        private Button deleteFR;
        private LinearLayout receivedFRButtonsContainer;

        public ViewHolder(ItemFriendBinding binding) {
            super(binding.getRoot());
            profileImageView = binding.profileImageView;
            screennameTextView = binding.screenNameTextView;
            usernameTextView = binding.usernameTextView;
            addFriendImageView = binding.addFriendImageView;
            pendingTextView = binding.pendingTextView;
            receivedFRButtonsContainer = binding.receivedFRButtonsContainer;
            acceptFR = binding.acceptButton;
            deleteFR = binding.deleteButton;
        }

        public void bind(ParseUser aUser, int relationStatus) throws Exception {
            position = getAdapterPosition();
            otherUser = aUser;
            otherUserId = aUser.getObjectId();
            otherUserUsername = aUser.getUsername();

            Log.i("bind", otherUserUsername + " " + relationStatus);
            switch (relationStatus) {
                case IS_STRANGER:
                    sendFRButtonListener();
                    break;
                case SENT_FR:
                    pendingTextView.setVisibility(View.VISIBLE);
                    break;
                case RECEIVED_FR:
                    receivedFRButtonsListener();
                    break;
                default:
                    break;
            }

            String screenName = aUser.get("screenName").toString();
            if (screenName != null) {
                screennameTextView.setText(screenName);
            }
            usernameTextView.setText(otherUserUsername);
        }

        private void sendFRButtonListener() {
            addFriendImageView.setVisibility(View.VISIBLE);
            addFriendImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(SEND_FR_CODE);
                    Log.i(TAG, "onclick " + otherUserUsername);
                }
            });
        }

        private void receivedFRButtonsListener() {
            receivedFRButtonsContainer.setVisibility(View.VISIBLE);
            deleteFR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(DELETE_FR_CODE);
                }
            });
        }

        private void showDialog(final int FRActionCode) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            String title = "";
            String message = "";
            switch (FRActionCode) {
                case SEND_FR_CODE:
                    title = "Send Friend Request";
                    message = "Want to send friend request to " + otherUserUsername + "?";
                    break;
                case DELETE_FR_CODE:
                    title = "Delete Friend Request";
                    message = "Want to delete friend request from " + otherUserUsername + "?";
                    break;
            }

            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FRAction(FRActionCode);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = alertDialog.create();
            dialog.show();
        }

        private void FRAction(int actionCode) {
            switch (actionCode) {
                case SEND_FR_CODE:
                    boolean requestSuccess = CurrentUserUtilities.sendFriendRequest(otherUser);
                    Log.i(TAG, "success: " + requestSuccess);
                    if (requestSuccess == true) {
                        addFriendImageView.setVisibility(View.GONE);
                        notifyItemChanged(position);
                    }
                    break;
                case DELETE_FR_CODE:
                    boolean deleteFRSuccess = CurrentUserUtilities.deleteFriendRequest(otherUser);
                    Log.i("deleteFR", "" + deleteFRSuccess);
                    if (deleteFRSuccess == true) {
                        notifyItemChanged(position);
                    }
            }

        }
    }
}
