package com.fbu.thefoodienetwork.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.models.Restaurant;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RESTAURANT = "selectedRestaurant";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Restaurant mRestaurant;
    //private String mParam2;

    public ComposeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ComposeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeFragment newInstance(Restaurant restaurant) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RESTAURANT, Parcels.wrap(restaurant));
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRestaurant = (Restaurant) Parcels.unwrap(getArguments().getParcelable(ARG_RESTAURANT));
            //mRestaurant = getArguments().getParcelable(ARG_RESTAURANT);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }
}