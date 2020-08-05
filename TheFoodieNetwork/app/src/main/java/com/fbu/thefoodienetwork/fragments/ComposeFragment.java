package com.fbu.thefoodienetwork.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.activities.SearchActivity;
import com.fbu.thefoodienetwork.databinding.FragmentComposeBinding;
import com.fbu.thefoodienetwork.keys.ParcelKeys;
import com.fbu.thefoodienetwork.keys.RequestCode;
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

public class ComposeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "ComposeFragment";
    private static final int EVERYONE = 0;
    private static final int FRIENDS = 1;
    private static Restaurant selectedRestaurant;
    private boolean recommended = true;
    private FragmentComposeBinding binding;
    private Spinner spinner;
    private boolean shareWithEveryone = true;
    private YoYo.YoYoString animation;

    public ComposeFragment() {
        // Required empty public constructor
    }

    public static ComposeFragment newInstance(Restaurant restaurant) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ParcelKeys.SELECTED_RESTAURANT, Parcels.wrap(restaurant));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedRestaurant = (Restaurant) Parcels.unwrap(getArguments().getParcelable(ParcelKeys.SELECTED_RESTAURANT));
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null) {
            selectedRestaurant = (Restaurant) Parcels.unwrap(getArguments().getParcelable(ParcelKeys.SELECTED_RESTAURANT));
            Log.i(TAG, "onStart " + selectedRestaurant.getName());
        }

        if (selectedRestaurant != null) {
            ratingListener(true);
            animation.stop();
        } else {

            animation = YoYo.with(Techniques.Shake)
                    .repeat(Animation.INFINITE).playOn(binding.searchIconImageView);

            ratingListener(false);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComposeBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        spinner = binding.simpleSpinner;

        binding.searchIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchRestaurant();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void ratingListener(boolean enable) {
        //if restaurant is selected let user use the rating bar else show message
        if (enable) {
            binding.restaurantInfoTextView.setText(selectedRestaurant.getName());
            binding.ratingBar.setEnabled(true);
            binding.ratingBar.setIsIndicator(false);
            binding.reviewEditText.setEnabled(true);
            binding.yesRadioButton.setEnabled(true);
            binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    binding.ratingTextView.setText(String.valueOf(ratingBar.getRating()));
                }
            });
            setSubmitButtonListener();
            spinner.setEnabled(true);
            setScopeSpinner();
            setRecommendRadioGroupListener();
        } else {

            binding.reviewEditText.setText("");
            binding.ratingBar.setRating(0);
            binding.ratingBar.setEnabled(false);
            binding.ratingBar.setIsIndicator(true);
            binding.reviewEditText.setEnabled(false);
            binding.yesRadioButton.setEnabled(false);
            binding.noRadioButton.setEnabled(false);
            spinner.setEnabled(false);

        }
    }

    private void setScopeSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.scopes_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setSubmitButtonListener() {
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
                saveReview(author, reviewText, reviewRating, recommended, shareWithEveryone);
            }
        });
    }

    private void setRecommendRadioGroupListener() {
        binding.recommendRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == binding.noRadioButton.getId()) {
                    recommended = false;
                }
                if (i == binding.yesRadioButton.getId()) {
                    recommended = true;
                }
            }
        });
    }

    private void saveReview( ParseUser author, String text, float rating, boolean recommend, boolean shareWithEveryone) {
        ParseReview review = new ParseReview();
        review.setAuthor(author);
        review.setRating(rating);
        review.setRecommend(recommend);
        review.setText(text);
        review.setGlobal(shareWithEveryone);
        checkRestaurantExistAndSave( review);
    }

    private void checkRestaurantExistAndSave( final ParseReview review) {
        ParseQuery<ParseRestaurant> parseRestaurantQuery = ParseQuery.getQuery(ParseRestaurant.class);
        parseRestaurantQuery.whereEqualTo(ParseRestaurant.ZOMATO_ID_KEY, selectedRestaurant.getId());
        parseRestaurantQuery.findInBackground(new FindCallback<ParseRestaurant>() {
            @Override
            public void done(List<ParseRestaurant> restaurants, ParseException e) {
                //if restaurant is not exist yet on Parse go ahead an save to Parse
                //else get the exist restaurant and set the review points to that
                if (restaurants.isEmpty() || e != null) {
                    review.setRestaurant(SaveRestaurant());
                } else {
                    review.setRestaurant(restaurants.get(0));
                }
                review.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving review", e);
                        } else {
                            Log.i(TAG, "Review save was success!!");
                            Toast.makeText(getContext(), "Successfully save review", Toast.LENGTH_LONG).show();
                            selectedRestaurant = null;
                            Log.i(TAG,"" + selectedRestaurant);
                            ComposeFragment.this.setArguments(null);
                            getFragmentManager().beginTransaction()
                                    .detach(ComposeFragment.this)
                                    .attach(ComposeFragment.this)
                                    .commitAllowingStateLoss();
                        }

                    }
                });
                return;
            }
        });
    }

    //Save restaurant to Parse
    private ParseRestaurant SaveRestaurant() {
        ParseRestaurant parseRestaurant = new ParseRestaurant(selectedRestaurant);
        parseRestaurant.set();
        parseRestaurant.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving restaurant", e);
                } else {
                    Log.i(TAG, "Restaurant save was success!!");
                }
            }
        });
        return parseRestaurant;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Log.i(TAG, position + " " + adapterView.getItemAtPosition(position));

        if (position == FRIENDS) {
            shareWithEveryone = false;
            return;
        }
        if (position == EVERYONE) {
            shareWithEveryone = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void goToSearchRestaurant() {
        Intent searchRestaurantIntent = new Intent(getActivity(), SearchActivity.class);
        getActivity().startActivityForResult(searchRestaurantIntent, RequestCode.RES_SEARCH_CODE);
    }

}