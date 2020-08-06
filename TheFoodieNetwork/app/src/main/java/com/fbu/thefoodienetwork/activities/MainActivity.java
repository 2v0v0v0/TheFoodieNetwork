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
import com.fbu.thefoodienetwork.keys.RequestCode;
import com.fbu.thefoodienetwork.models.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private final static String HOME_FRAG_TAG = "HomeFragment";
    private final static String GLOBE_FRAG_TAG = "GlobeFragment";
    private final static String COMPOSE_FRAG_TAG = "ComposeFragment";

    private final Fragment homeFragment = new HomeFragment();
    private final Fragment globeFragment = new GlobeFragment();
    private final Fragment composeFragment = new ComposeFragment();

    final private FragmentManager fragmentManager = getSupportFragmentManager();
    private ActivityMainBinding binding;
    private Fragment active = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        fragmentManager.beginTransaction().add(R.id.containerFrameLayout, composeFragment, COMPOSE_FRAG_TAG).hide(composeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.containerFrameLayout, globeFragment, GLOBE_FRAG_TAG).hide(globeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.containerFrameLayout, homeFragment, HOME_FRAG_TAG).commit();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_compose:
                        fragmentManager.beginTransaction().hide(active).show(composeFragment).commit();
                        active = composeFragment;
                        return true;

                    case R.id.action_globe:
                        fragmentManager.beginTransaction().hide(active).show(globeFragment).commit();
                        active = globeFragment;
                        return true;

                    case R.id.action_home:
                        fragmentManager.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        return true;
                }

                return false;
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
        startActivityForResult(searchRestaurantIntent, RequestCode.RES_SEARCH_CODE);
    }

    private void goToSearchFriend() {
        Intent searchFriend = new Intent(MainActivity.this, SearchFriendActivity.class);
        startActivity(searchFriend);
    }

    private void goToProfile() {
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        profileIntent.putExtra(ParcelKeys.SELECTED_USER, Parcels.wrap(CurrentUserUtilities.getInstance().getCurrentUser()));
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
        //TODO progress bar
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();

        CurrentUserUtilities.getInstance().cleanUp();

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
        if (resultCode == RESULT_OK && requestCode == RequestCode.RES_SEARCH_CODE) {

            Restaurant restaurant = Parcels.unwrap(data.getParcelableExtra(ParcelKeys.SELECTED_RESTAURANT));
            Log.i(TAG, restaurant.toString());

            Bundle bundle = new Bundle();
            bundle.putParcelable(ParcelKeys.SELECTED_RESTAURANT, data.getParcelableExtra(ParcelKeys.SELECTED_RESTAURANT)); // Put anything what you want

            composeFragment.setArguments(bundle);

            binding.bottomNavigation.setSelectedItemId(R.id.action_compose);

        }
    }

}