package com.fbu.thefoodienetwork.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.models.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private static final String TAG = "LocationAdapter";
    private Context context;
    private List<Location> locationList;
    private OnClickLocationListener onClickLocationListener;

    public LocationAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;

        try {
            this.onClickLocationListener = ((OnClickLocationListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnClickLocationListener.");
        }
    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View locationView = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(locationView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder");
        final Location location = locationList.get(position);
        holder.bind(location);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "on click item: " + position);
                onClickLocationListener.onClickLocation(position);
            }
        });
    }

    public interface OnClickLocationListener {
        void onClickLocation(int position);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView locationTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTitle = itemView.findViewById(R.id.locationTextView);
        }

        public void bind(Location location) {
            Log.i(TAG, "bind");
            locationTitle.setText(location.getTitle());
        }
    }
}
