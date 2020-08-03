package com.fbu.thefoodienetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.OnSwipeTouchListener;
import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ActivityMainBinding;
import com.fbu.thefoodienetwork.fragments.ComposeFragment;
import com.fbu.thefoodienetwork.fragments.GlobeFragment;
import com.fbu.thefoodienetwork.fragments.HomeFragment;
import com.fbu.thefoodienetwork.keys.ParcelKeys;
import com.fbu.thefoodienetwork.models.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
        setSwipeListener(binding.containerFrameLayout);

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

    private void setSwipeListener(View view){
        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                //TODO swipe down to refresh
                Toast.makeText(MainActivity.this, "refresh", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "Search for Restaurant", Toast.LENGTH_SHORT).show();
                goToSearchRestaurant();
            }

            @Override
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "Search for People", Toast.LENGTH_SHORT).show();
                goToSearchFriend();
            }
        });
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
                goToSearchRestaurant();
                return true;
            case R.id.menu_search_friend:
                goToSearchFriend();
                return true;
            case R.id.menu_logout:
                logoutUser();
                return true;
            case R.id.menu_profile:
                goToProfile();
                return true;
            case R.id.menu_bookmark:
                goToBookmark();
                return true;
            case R.id.menu_friend_list:
                goToFriendList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void goToSearchRestaurant() {
        Intent searchRestaurantIntent = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(searchRestaurantIntent, SEARCH_CODE);
    }

    private void goToSearchFriend() {
        Intent searchFriend = new Intent(MainActivity.this, SearchFriendActivity.class);
        startActivity(searchFriend);
    }

    private void goToProfile() {
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        profileIntent.putExtra(ParcelKeys.SELECTED_USER, Parcels.wrap(CurrentUserUtilities.currentUser));
        startActivity(profileIntent);
    }

    private void goToBookmark() {
        Intent bookmarkIntent = new Intent(MainActivity.this, BookmarkActivity.class);
        startActivity(bookmarkIntent);
    }

    private void goToFriendList() {
        Intent friendListIntent = new Intent(MainActivity.this, FriendListActivity.class);
        startActivity(friendListIntent);
    }

    private void logoutUser() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);

        ParseInstallation.getCurrentInstallation().remove("user");
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SEARCH_CODE) {
            Restaurant restaurant = (Restaurant) Parcels.unwrap(data.getParcelableExtra(ParcelKeys.SELECTED_RESTAURANT));
            Log.i(TAG, restaurant.toString());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ComposeFragment composeFragment = ComposeFragment.newInstance(restaurant);
            binding.bottomNavigation.setSelectedItemId(R.id.action_compose);
            ft.replace(R.id.containerFrameLayout, composeFragment);
            ft.commit();
        }
    }

}