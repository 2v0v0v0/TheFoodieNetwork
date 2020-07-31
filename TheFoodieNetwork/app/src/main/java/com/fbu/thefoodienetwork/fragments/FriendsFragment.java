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
import com.fbu.thefoodienetwork.keys.UserKeys;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";
    private FragmentFriendsBinding binding;
    private FriendAdapter friendAdapter;
    private List<ParseUser> friendList = new ArrayList<>();

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queryFriend();
    }

    private void queryFriend() {
        ParseRelation relation = CurrentUserUtilities.currentUser.getRelation(UserKeys.FRIENDS);
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> results, ParseException e) {

                if (e != null) {
                    Log.i(TAG, "error: " + e);
                    return;
                }

                friendList.addAll(results);
                Log.i(TAG, friendList.toString());

                binding.friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                friendAdapter = new FriendAdapter(getContext(), friendList);
                binding.friendRecyclerView.setAdapter(friendAdapter);
            }
        });
    }
}