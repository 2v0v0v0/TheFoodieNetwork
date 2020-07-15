package com.fbu.thefoodienetwork.adapters;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.models.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private static final String TAG = "RestaurantAdapter";
    private Context context;
    private List<Restaurant> restaurantList;
    private RestaurantAdapter.OnClickRestaurantListener onClickRestaurantListener;

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;

        try {
            this.onClickRestaurantListener = ((RestaurantAdapter.OnClickRestaurantListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnClickRestaurantListener.");
        }
    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View restaurantView = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantAdapter.ViewHolder(restaurantView);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder");
        final Restaurant restaurant = restaurantList.get(position);
        holder.bind(restaurant);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "on click item: " + position);
                onClickRestaurantListener.onClickRestaurant(position);
            }
        });
    }

    public interface OnClickRestaurantListener {
        void onClickRestaurant(int position);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView cuisinesTextView;
        public TextView addressTextView;
        public Button reviewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            cuisinesTextView = itemView.findViewById(R.id.cuisinesTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            reviewButton = itemView.findViewById(R.id.reviewButton);
        }

        public void bind(Restaurant restaurant) {
            Log.i(TAG, "bind");
            nameTextView.setText(restaurant.getName());
            cuisinesTextView.setText(restaurant.getCuisines());
            addressTextView.setText(restaurant.getAddress());

            reviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: go to compose fragment
                    Log.i(TAG, "on write review button clicked of " + getAdapterPosition());
                }
            });
        }
    }
}
