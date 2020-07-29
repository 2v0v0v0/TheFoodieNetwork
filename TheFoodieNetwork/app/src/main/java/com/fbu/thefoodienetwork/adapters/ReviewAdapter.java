package com.fbu.thefoodienetwork.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fbu.thefoodienetwork.MyBounceInterpolator;
import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ItemReviewBinding;
import com.fbu.thefoodienetwork.keys.UserKeys;
import com.fbu.thefoodienetwork.models.ParseRestaurant;
import com.fbu.thefoodienetwork.models.ParseReview;
import com.fbu.thefoodienetwork.models.RelativeTime;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static final String TAG = "ReviewAdapter";
    private Context context;
    private List<ParseReview> reviewsList;

    public ReviewAdapter(Context context, List<ParseReview> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
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
        ParseReview aReview = reviewsList.get(position);
        try {
            holder.bind(aReview);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final double AMPLITUDE = 0.2;
        private static final double FREQUENCY = 20;
        private final ItemReviewBinding binding;
        private ImageView heartButton;
        private ImageView bookmarkButton;
        private Animation animation;
        private MyBounceInterpolator interpolator;

        public ViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            heartButton = binding.heartImageView;
            bookmarkButton = binding.bookmarkImageView;

            //animation
            animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
            interpolator = new MyBounceInterpolator(AMPLITUDE, FREQUENCY);
            animation.setInterpolator(interpolator);
        }

        public void bind(ParseReview review) throws ParseException {

            //Author
            ParseUser author = review.getAuthor();
            binding.usernameTextView.setText(author.getUsername());
            ParseFile profileImage = author.getParseFile(UserKeys.PROFILE_IMAGE);
            if (profileImage != null) {
                Glide.with(context).load(profileImage.getUrl()).centerCrop().circleCrop().into(binding.userProfilePic);
            } else {
                Glide.with(context).load(R.drawable.placeholder).centerCrop().circleCrop().into(binding.userProfilePic);
            }

            //Timestamp
            try {
                binding.timeTextView.setText(RelativeTime.getRelativeTimeAgo(review.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Review
            binding.ratingBar.setRating(review.getRating());
            binding.reviewTextView.setText(review.getText());
            binding.recommendTextView.setText(String.format("Recommend: %s", review.getRecommend() ? "YES" : "NO"));

            //Restaurant
            ParseRestaurant restaurant = review.getRestaurant();
            binding.restaurantName.setText(restaurant.getName());
            binding.restaurantLocation.setText(restaurant.getAddress());

            //Button state
            if (review.isBookmarked){
                bookmarkButton.setSelected(true);
            }
            setButtonListener();
        }

        private void setButtonListener() {

            //heart button
            heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bookmarkButton.clearAnimation();
                    heartButton.startAnimation(animation);

                    if (heartButton.isSelected()) {
                        heartButton.setSelected(false);
                    } else {
                        heartButton.setSelected(true);
                    }

                }
            });

            //bookmark button
            bookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    heartButton.clearAnimation();
                    bookmarkButton.startAnimation(animation);

                    if (bookmarkButton.isSelected()) {
                        bookmarkButton.setSelected(false);
                    } else {
                        bookmarkButton.setSelected(true);
                    }

                }
            });
        }

    }
}
