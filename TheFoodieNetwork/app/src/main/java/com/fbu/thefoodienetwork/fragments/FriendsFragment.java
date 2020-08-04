package com.fbu.thefoodienetwork.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.adapters.FriendAdapter;
import com.fbu.thefoodienetwork.databinding.FragmentFriendsBinding;
import com.parse.ParseUser;

import java.util.List;

public class FriendsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "FriendsFragment";
    private static final int AtoZ = 1;
    private static final int ZtoA = 2;
    private static List<ParseUser> friendList;
    private FragmentFriendsBinding binding;
    private FriendAdapter friendAdapter;

    public FriendsFragment() {
        // Required empty public constructor
    }

    private void selectionSortAToZ() {

        for (int j = 0; j < friendList.size() - 1; j++) {

            int min = j;
            for (int k = j + 1; k < friendList.size(); k++)
                if (friendList.get(k).getUsername().compareToIgnoreCase(friendList.get(min).getUsername()) < 0)
                    min = k;

            ParseUser temp = friendList.get(j);
            friendList.set(j, friendList.get(min));
            friendList.set(min, temp);
        }

        friendAdapter.notifyDataSetChanged();
    }

    private void selectionSortZToA() {

        for (int j = 0; j < friendList.size() - 1; j++) {

            int max = j;
            for (int k = j + 1; k < friendList.size(); k++)
                if (friendList.get(k).getUsername().compareToIgnoreCase(friendList.get(max).getUsername()) > 0)
                    max = k;

            ParseUser temp = friendList.get(j);
            friendList.set(j, friendList.get(max));
            friendList.set(max, temp);
        }

        friendAdapter.notifyDataSetChanged();
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

        friendList = CurrentUserUtilities.getInstance().getFriendParseUserList();

        binding.friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendAdapter = new FriendAdapter(getContext(), friendList);
        binding.friendRecyclerView.setAdapter(friendAdapter);

        setSpinner();

    }

    private void setSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.sortSpinner.setAdapter(adapter);
        binding.sortSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, i + " " + adapterView.getItemAtPosition(i));
        if (i == AtoZ) {
            selectionSortAToZ();
            return;
        }
        if (i == ZtoA) {
            selectionSortZToA();
            return;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}