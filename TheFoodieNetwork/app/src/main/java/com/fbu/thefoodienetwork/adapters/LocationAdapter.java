package com.fbu.thefoodienetwork.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ItemLocationBinding;
import com.fbu.thefoodienetwork.models.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private static final String TAG = "LocationAdapter";
    private Context context;
    private List<Location> locationList;

    public LocationAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View locationView = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);

        return new ViewHolder(locationView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder");
        Location location = locationList.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView locationTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTitle = itemView.findViewById(R.id.locationTextView);
        }

        public void bind(Location location) {
            Log.i(TAG,"bind");
            locationTitle.setText(location.getTitle());
        }
    }
}
