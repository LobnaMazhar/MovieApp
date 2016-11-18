package com.mal.lobna.movieapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mal.lobna.movieapp.Adapter.HomeAdapter;
import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Data.MovieDataSource;
import com.mal.lobna.movieapp.FetchMovies;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.Activity.MovieViewActivity;
import com.mal.lobna.movieapp.OnItemClickListener;
import com.mal.lobna.movieapp.R;

import java.util.ArrayList;

/**
 * Created by Lobna on 05-Oct-16.
 */
public class HomeFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final String LOG_TAG = HomeFragment.class.getSimpleName();

    ArrayList<Movie> moviesEntries;
    RecyclerView moviesHomeView;
    public static HomeAdapter homeAdapter;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeToRefresh;

    public final static String ORIGINAL_TITLE_KEY = "Original title";
    public final static String MOVIE_POSTER_KEY = "Movie Poster";
    public final static String OVERVIEW_KEY = "Overview";
    public final static String AVERAGE_VOTING_KEY = "Average Voting";
    public final static String RELEASE_DATE_KEY = "Release Date";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");

        moviesEntries = new ArrayList<>();

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        moviesHomeView = (RecyclerView) rootView.findViewById(R.id.moviesHomeRecyclerView);
        moviesHomeView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        swipeToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(R.color.colorPrimaryDark),getResources().getColor(R.color.colorAccent));
        swipeToRefresh.setOnRefreshListener(this);

        homeAdapter = new HomeAdapter(getActivity(), moviesEntries, new OnItemClickListener() {
            @Override
            public void onItemClick(Movie item) {
                Intent goToMovieViewActivity = new Intent(getActivity(), MovieViewActivity.class);
                goToMovieViewActivity.putExtra(ORIGINAL_TITLE_KEY, item.getOriginal_title());
                goToMovieViewActivity.putExtra(MOVIE_POSTER_KEY, item.getPoster_path());
                goToMovieViewActivity.putExtra(OVERVIEW_KEY, item.getOverview());
                goToMovieViewActivity.putExtra(AVERAGE_VOTING_KEY, item.getVote_average());
                goToMovieViewActivity.putExtra(RELEASE_DATE_KEY, item.getRelease_date());
                startActivity(goToMovieViewActivity);
            }
        });
        moviesHomeView.setAdapter(homeAdapter);

        updateMovies();

        return rootView;
    }

    public void updateMovies() {
        Context context = MovieApplication.getMovieApp().getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortBy = sharedPreferences.getString(context.getString(R.string.sortMoviesBy_prefKey), context.getResources().getString(R.string.sortMoviesBy_defaultPrefValue));
        if(sortBy.equals(R.string.favourites_prefValueID)){
            ArrayList<Movie> movies = MovieDataSource.getInstance().getFavourites();
            if (movies != null) {
                homeAdapter.movies.clear();
                for (Movie movie : movies)
                    homeAdapter.movies.add(movie);
            }
            swipeToRefresh.setRefreshing(false);
        }else{
            new FetchMovies(getActivity(), progressDialog, sortBy, swipeToRefresh).execute();
        }
    }

    @Override
    public void onRefresh() {
        updateMovies();
    }
}
