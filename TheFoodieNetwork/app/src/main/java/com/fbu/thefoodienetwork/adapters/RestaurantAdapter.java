package com.fbu.thefoodienetwork.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.models.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private static final String TAG = "RestaurantAdapter";
    private Context context;
    private List<Restaurant> restaurantList;
    private RestaurantAdapter.OnClickRestaurantListener onClickRestaurantListener;
    private int selectedItem = 0;
    private int lastSelected = 0;

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
        final View background = holder.itemView.findViewById(R.id.parent);
        Log.i(TAG, "onBindViewHolder");
        final Restaurant restaurant = restaurantList.get(position);
        holder.bind(restaurant);
        //If is selected the color change
        int backgroundColor = (position == selectedItem) ? R.color.colorLightBlue : R.color.colorMain;

        background.setBackgroundColor(ContextCompat.getColor(context, backgroundColor));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "on select item: " + position);
                onClickRestaurantListener.onClickRestaurant(position);
                onClickRestaurantListener.onClickMoreInfo(false);

                //Save the position of the last selected item
                lastSelected = selectedItem;
                //Save the position of the current selected item
                selectedItem = position;
                //This update the last item selected
                notifyItemChanged(lastSelected);
                //This update the item selected
                notifyItemChanged(selectedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public interface OnClickRestaurantListener {
        void onClickRestaurant(int position);
        void onClickMoreInfo(boolean indicator);//check if the user select the restaurant or click on more info button
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView cuisinesTextView;
        private TextView addressTextView;
        private Button moreInfoButton;
        private Restaurant selectedRestaurant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            cuisinesTextView = itemView.findViewById(R.id.cuisinesTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            moreInfoButton = itemView.findViewById(R.id.moreInfoButton);
        }

        public void bind(Restaurant restaurant) {
            selectedRestaurant = restaurant;
            Log.i(TAG, "bind");
            nameTextView.setText(restaurant.getName());
            cuisinesTextView.setText(restaurant.getCuisines());
            addressTextView.setText(restaurant.getAddress());

            moreInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "on more info button clicked of " + getAdapterPosition());
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onClickRestaurantListener.onClickRestaurant(position);
                        onClickRestaurantListener.onClickMoreInfo(true);
                    }
                }
            });

        }

    }
}
