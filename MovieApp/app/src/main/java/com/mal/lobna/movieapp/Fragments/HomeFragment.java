package com.mal.lobna.movieapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mal.lobna.movieapp.Adapter.HomeAdapter;
import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Data.MovieContract;
import com.mal.lobna.movieapp.Data.MovieDataSource;
import com.mal.lobna.movieapp.Listeners.CallbackListener;
import com.mal.lobna.movieapp.Managers.MovieManager;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.Activity.MovieViewActivity;
import com.mal.lobna.movieapp.Listeners.MoviesListener;
import com.mal.lobna.movieapp.Listeners.OnMovieClickListener;
import com.mal.lobna.movieapp.R;
import com.mal.lobna.movieapp.Utilities.Utilities;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lobna on 05-Oct-16.
 */
public class HomeFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener, MoviesListener {

    private final String LOG_TAG = HomeFragment.class.getSimpleName();

    RecyclerView moviesHomeView;
    public HomeAdapter homeAdapter;
    private ArrayList<Movie> movies;

    private SwipeRefreshLayout swipeToRefresh;

    private String sortByPreference;

    private final static int INVALID_POSITION = -1010;
    private int position = INVALID_POSITION;
    private String SELECTED_KEY_POSITION = "selectedPositionKey";

    private Bundle mSavedInstanceState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        moviesHomeView = (RecyclerView) rootView.findViewById(R.id.moviesHomeRecyclerView);
        moviesHomeView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        swipeToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent));
        swipeToRefresh.setOnRefreshListener(this);

        updateMovies();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortByPreference = sharedPreferences.getString(getActivity().getString(R.string.sortMoviesBy_prefKey), getActivity().getResources().getString(R.string.sortMoviesBy_defaultPrefValue));

        if ((mSavedInstanceState = savedInstanceState) != null && savedInstanceState.containsKey(SELECTED_KEY_POSITION))
            position = savedInstanceState.getInt(SELECTED_KEY_POSITION);

        return rootView;
    }

    public void updateMovies() {
        HomeAdapter.clearAdapter();
        MovieManager.getInstance().getMovies(this);
        movies = MovieManager.getMoviesList();
    }

    @Override
    public void onResume() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = sharedPreferences.getString(getActivity().getString(R.string.sortMoviesBy_prefKey), getActivity().getResources().getString(R.string.sortMoviesBy_defaultPrefValue));

        if (sortBy != sortByPreference) {
            sortByPreference = sortBy;
            updateMovies();
        }

        if (getActivity().findViewById(R.id.movieViewLayout) != null) {
            if (position != GridView.INVALID_POSITION) {
                moviesHomeView.scrollToPosition(position);
                if (movies != null && movies.size() != 0 && position != INVALID_POSITION)
                    ((CallbackListener) getActivity()).onItemSelected(movies.get(position));
            }
        }

        super.onResume();
    }

    @Override
    public void onRefresh() {
        updateMovies();
    }

    @Override
    public void onDownloadFinished(ArrayList<Movie> movies) {
        swipeToRefresh.setRefreshing(false);

        if(mSavedInstanceState == null) {
            if (getActivity().findViewById(R.id.movieViewLayout) != null && movies.size() != 0) {
                ((CallbackListener) getActivity()).onItemSelected(movies.get(0));
            }
        }
        this.movies = movies;
        onOrientationChange(getResources().getConfiguration().orientation, movies);
    }

    public void onOrientationChange(int orientation, final ArrayList<Movie> movies) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            moviesHomeView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            moviesHomeView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }
        homeAdapter = new HomeAdapter(getActivity(), movies, new OnMovieClickListener() {
            @Override
            public void onMovieClick(final Movie item) {
                item.setFavourite(MovieDataSource.getInstance().isFav(item.getId()));

                ((CallbackListener) getActivity()).onItemSelected(item);
                position = movies.indexOf(item);
            }
        });
        moviesHomeView.setAdapter(homeAdapter);
    }

    @Override
    public void onFail(Exception e) {
        swipeToRefresh.setRefreshing(false);
        Utilities.noInternet(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (position != GridView.INVALID_POSITION)
            outState.putInt(SELECTED_KEY_POSITION, position);
    }
}
