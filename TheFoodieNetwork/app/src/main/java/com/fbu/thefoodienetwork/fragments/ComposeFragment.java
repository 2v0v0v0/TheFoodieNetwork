package com.fbu.thefoodienetwork.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.FragmentComposeBinding;
import com.fbu.thefoodienetwork.models.Restaurant;

import org.parceler.Parcels;

public class ComposeFragment extends Fragment {
    private FragmentComposeBinding binding;
    private static final String ARG_RESTAURANT = "selectedRestaurant";
    private Restaurant mRestaurant;

    public ComposeFragment() {
        // Required empty public constructor
    }

    public static ComposeFragment newInstance(Restaurant restaurant) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RESTAURANT, Parcels.wrap(restaurant));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRestaurant = (Restaurant) Parcels.unwrap(getArguments().getParcelable(ARG_RESTAURANT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // binding.
        binding = FragmentComposeBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        if (mRestaurant != null){
            binding.restaurantInfoTextView.setText(mRestaurant.getName());
        }
        return view;
    }
}