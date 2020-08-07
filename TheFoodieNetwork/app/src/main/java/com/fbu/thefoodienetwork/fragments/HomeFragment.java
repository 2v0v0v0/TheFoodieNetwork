package com.fbu.thefoodienetwork.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.EndlessRecyclerViewScrollListener;
import com.fbu.thefoodienetwork.OnSwipeTouchListener;
import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.activities.BookmarkActivity;
import com.fbu.thefoodienetwork.activities.MainActivity;
import com.fbu.thefoodienetwork.activities.SearchActivity;
import com.fbu.thefoodienetwork.adapters.LocationAdapter;
import com.fbu.thefoodienetwork.adapters.ReviewAdapter;
import com.fbu.thefoodienetwork.databinding.FragmentHomeBinding;
import com.fbu.thefoodienetwork.models.ParseReview;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final int REVIEW_LIMIT = 10;
    private final String TAG = "HomeFragment";
    protected ReviewAdapter reviewAdapter;
    protected List<ParseReview> allReviews;
    private FragmentHomeBinding binding;
    private List<ParseUser> currentUserAndFriends = new ArrayList<>();
    private RecyclerView reviewRecyclerView;
    private LinearLayoutManager layoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        reviewRecyclerView = binding.reviewRecyclerView;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutManager = new LinearLayoutManager(getContext());

        allReviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(getContext(), allReviews);
        reviewRecyclerView.setAdapter(reviewAdapter);
        reviewRecyclerView.setLayoutManager(layoutManager);

        currentUserAndFriends = CurrentUserUtilities.getInstance().getFriendParseUserList();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentUserAndFriends.add(CurrentUserUtilities.getInstance().getCurrentUser());

                pullRefresh();

                queryReviews();

                infiniteScroll();

            }
        }, 1000);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void pullRefresh() {
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allReviews.clear();
                queryReviews();
                Log.i(TAG, "fetching data");
                binding.swipeContainer.setRefreshing(false);
            }
        });

        binding.swipeContainer.setColorSchemeResources(
                R.color.colorMediumBlue,
                R.color.colorGreen,
                R.color.colorPinkAccent);
    }


    private void queryReviews() {
        binding.progressBar.setVisibility(View.VISIBLE);

        ParseQuery<ParseReview> query = ParseQuery.getQuery(ParseReview.class);

        query.include(ParseReview.AUTHOR_KEY);
        query.include(ParseReview.RESTAURANT_KEY);

        query.whereContainedIn(ParseReview.AUTHOR_KEY, currentUserAndFriends);
        query.whereEqualTo(ParseReview.GLOBAL_KEY, false);

        query.setLimit(REVIEW_LIMIT);
        query.addDescendingOrder(ParseReview.CREATED_AT_KEY);

        query.findInBackground(new FindCallback<ParseReview>() {
            @Override
            public void done(List<ParseReview> reviewList, ParseException e) {

                binding.progressBar.setVisibility(View.GONE);

                if (e != null) {
                    Log.e(TAG, "Issue with getting reviews");
                    return;
                }

                if (reviewList.isEmpty()) {
                    binding.emptyHomeView.setVisibility(View.VISIBLE);
                    return;
                }

                for (ParseReview review : reviewList) {

                    review.getHeartRelation();

                    if (BookmarkActivity.bookmarkList.contains(review)) {
                        review.setBookmark(true);
                    }

                    Log.i(TAG, "Post: " + review.getText() + ", username: " + review.getAuthor().getUsername()
                            + " saved: " + review.getBookmark());
                }
                allReviews.addAll(reviewList);
                reviewAdapter.notifyDataSetChanged();
            }
        });

    }

    private void loadMoreData() {

        binding.progressBar.setVisibility(View.VISIBLE);

        ParseQuery<ParseReview> query = ParseQuery.getQuery(ParseReview.class);

        query.include(ParseReview.AUTHOR_KEY);
        query.include(ParseReview.RESTAURANT_KEY);

        query.whereEqualTo(ParseReview.GLOBAL_KEY, false);
        query.setLimit(REVIEW_LIMIT);

        query.whereContainedIn(ParseReview.AUTHOR_KEY, currentUserAndFriends);
        query.whereLessThan(ParseReview.CREATED_AT_KEY, allReviews.get(allReviews.size() - 1).getCreatedAt());
        query.addDescendingOrder(ParseReview.CREATED_AT_KEY);

        query.findInBackground(new FindCallback<ParseReview>() {
            @Override
            public void done(List<ParseReview> reviewList, ParseException e) {

                binding.progressBar.setVisibility(View.GONE);

                if (e != null) {
                    Log.e(TAG, "Issue with getting reviews");
                    return;
                }

                for (ParseReview review : reviewList) {

                    review.getHeartRelation();

                    if (BookmarkActivity.bookmarkList.contains(review)) {
                        review.setBookmark(true);
                    }

                    Log.i(TAG, "Post: " + review.getText() + ", username: " + review.getAuthor().getUsername()
                            + " saved: " + review.getBookmark());
                }
                allReviews.addAll(reviewList);
                reviewAdapter.notifyDataSetChanged();
            }
        });
    }

    public void infiniteScroll() {

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                loadMoreData();
            }
        };

        binding.reviewRecyclerView.addOnScrollListener(scrollListener);
    }

}