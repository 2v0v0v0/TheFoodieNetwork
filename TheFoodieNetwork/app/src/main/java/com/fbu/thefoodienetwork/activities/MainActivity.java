package com.fbu.thefoodienetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ActivityMainBinding;
import com.fbu.thefoodienetwork.fragments.ComposeFragment;
import com.fbu.thefoodienetwork.fragments.GlobeFragment;
import com.fbu.thefoodienetwork.fragments.HomeFragment;
import com.fbu.thefoodienetwork.models.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import org.parceler.Parcels;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private final static int SEARCH_CODE = 55;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FragmentManager fragmentManager = getSupportFragmentManager();

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_compose:
                        fragment = new ComposeFragment();
                        Log.i(TAG, "menu item: " + "compose");
                        break;
                    case R.id.action_globe:
                        fragment = new GlobeFragment();
                        Log.i(TAG, "menu item: " + "globe");
                        break;
                    case R.id.action_home:
                    default:
                        fragment = new HomeFragment();
                        Log.i(TAG, "menu item: " + "home");
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.containerFrameLayout, fragment).commit();
                return true;
            }
        });
        // Set default selection
        binding.bottomNavigation.setSelectedItemId(R.id.action_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                goToSearchActivity();
                return true;
            case R.id.menu_logout:
                logoutUser();
                return true;
            case R.id.menu_profile:
                //TODO: navigate to user profile
                return true;
            case R.id.menu_bookmark:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToSearchActivity() {
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(i, SEARCH_CODE);
    }

    private void logoutUser() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SEARCH_CODE) {
            Restaurant restaurant = (Restaurant) Parcels.unwrap(data.getParcelableExtra("selectedRestaurant"));
            Log.i(TAG, restaurant.toString());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ComposeFragment composeFragment = ComposeFragment.newInstance(restaurant);
            binding.bottomNavigation.setSelectedItemId(R.id.action_compose);
            ft.replace(R.id.containerFrameLayout, composeFragment);
            ft.commit();
        }
    }
}