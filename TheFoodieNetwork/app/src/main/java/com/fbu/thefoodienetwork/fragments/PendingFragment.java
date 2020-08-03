package com.fbu.thefoodienetwork.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.adapters.FriendAdapter;
import com.fbu.thefoodienetwork.databinding.FragmentFriendsBinding;
import com.fbu.thefoodienetwork.databinding.FragmentPendingBinding;
import com.fbu.thefoodienetwork.databinding.FragmentRequestsBinding;
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

public class PendingFragment extends Fragment {
    private static final String TAG = "PendingFragment";
    private FragmentPendingBinding binding;
    private FriendAdapter friendAdapter;
    private List<ParseUser> pendingList;

    public PendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendingBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pendingList = CurrentUserUtilities.getInstance().getPendingParseUserList();

        if(pendingList.isEmpty()){
            binding.noPendingText.setVisibility(View.VISIBLE);
            return;
        }

        binding.pendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendAdapter = new FriendAdapter(getContext(), pendingList);
        binding.pendingRecyclerView.setAdapter(friendAdapter);
    }

}