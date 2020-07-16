package com.fbu.thefoodienetwork.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.thefoodienetwork.databinding.ItemReviewBinding;
import com.fbu.thefoodienetwork.models.ParseRestaurant;
import com.fbu.thefoodienetwork.models.ParseReview;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static final String TAG = "ReviewAdapter";
    private Context context;
    private List<ParseReview> reviewtList;

    public ReviewAdapter(Context context, List<ParseReview> reviewtList) {
        this.context = context;
        this.reviewtList = reviewtList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemReviewBinding binding = ItemReviewBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        ParseReview aReview = reviewtList.get(position);
        try {
            holder.bind(aReview);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return reviewtList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemReviewBinding binding;

        public ViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ParseReview review) throws ParseException {
            //Author
            ParseUser author = review.getAuthor();
            binding.usernameTextView.setText(author.getUsername());

            //Review
            binding.ratingBar.setRating(review.getRating());
            binding.reviewTextView.setText(review.getText());

            //Restaurant
            ParseRestaurant restaurant = review.getRestaurant();
            binding.restaurantName.setText(restaurant.getName());
            binding.restaurantLocation.setText(restaurant.getAddress());
        }

    }
}
