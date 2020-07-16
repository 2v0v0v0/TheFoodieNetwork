package com.fbu.thefoodienetwork.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.FragmentComposeBinding;
import com.fbu.thefoodienetwork.models.ParseRestaurant;
import com.fbu.thefoodienetwork.models.ParseReview;
import com.fbu.thefoodienetwork.models.Restaurant;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

public class ComposeFragment extends Fragment {
    private static final String TAG = "ComposeFragment";
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
            ratingListener(true);
        }else {
            ratingListener(false);
        }
        return view;
    }

    private void ratingListener(boolean enable){
        //if restaurant is selected let user use the rating bar else show message
        if(enable == true){
            binding.restaurantInfoTextView.setText(mRestaurant.getName());
            binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    binding.ratingTextView.setText(String.valueOf(ratingBar.getRating()));
                }
            });
            submitButtonListener();
        }else {
            binding.ratingBar.setEnabled(false);
            binding.ratingBar.setIsIndicator(true);
            binding.reviewEditText.setEnabled(false);
        }
        //TODO: set up some message
    }

    private void submitButtonListener(){
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reviewText = binding.reviewEditText.getText().toString();
                //check if review text is empty
                if (reviewText.isEmpty()) {
                    Toast.makeText(getContext(), "Review cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                float reviewRating = binding.ratingBar.getRating();
                ParseUser author = ParseUser.getCurrentUser();
                saveReview(mRestaurant, author, reviewText, reviewRating);
            }
        });
    }

    private void saveReview(Restaurant selectedRestaurant, ParseUser author, String text, float rating){
        ParseReview review = new ParseReview();
        review.setAuthor(author);
        review.setRating(rating);
        review.setText(text);
        checkRestaurantExistAndSave(selectedRestaurant, review);
    }

    private void checkRestaurantExistAndSave (final Restaurant selectedRestaurant, final ParseReview review){
        ParseQuery<ParseRestaurant> parseRestaurantQuery = ParseQuery.getQuery(ParseRestaurant.class);
        parseRestaurantQuery.whereEqualTo(ParseRestaurant.ZOMATO_ID_KEY, selectedRestaurant.getId());
        parseRestaurantQuery.findInBackground(new FindCallback<ParseRestaurant>() {
            @Override
            public void done(List<ParseRestaurant> restaurants, ParseException e) {
                //if restaurant is not exist yet on Parse go ahead an save to Parse
                //else get the exist restaurant and set the review points to that
                if(restaurants.isEmpty() || e != null){
                    review.setRestaurant(SaveRestaurant(selectedRestaurant));
                }else {
                    review.setRestaurant(restaurants.get(0));
                }
                review.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving review", e);
                        }else {
                            Log.i(TAG, "Review save was success!!");
                        }
                    }
                });
                return;
            }
        });
    }

    //Save restaurant to Parse
    private ParseRestaurant SaveRestaurant(Restaurant selectedRestaurant){
        ParseRestaurant parseRestaurant = new ParseRestaurant(selectedRestaurant);
        parseRestaurant.set();
        parseRestaurant.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving restaurant", e);
                }else {
                    Log.i(TAG, "Restaurant save was success!!");
                }
            }
        });
        return parseRestaurant;
    }


}