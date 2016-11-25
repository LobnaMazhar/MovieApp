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
import android.widget.Toast;

import com.mal.lobna.movieapp.Adapter.HomeAdapter;
import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Data.MovieContract;
import com.mal.lobna.movieapp.Data.MovieDataSource;
import com.mal.lobna.movieapp.Managers.MovieManager;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.Activity.MovieViewActivity;
import com.mal.lobna.movieapp.Listeners.MoviesListener;
import com.mal.lobna.movieapp.Listeners.OnMovieClickListener;
import com.mal.lobna.movieapp.R;
import com.mal.lobna.movieapp.Utilities.Utilities;

import java.util.ArrayList;

/**
 * Created by Lobna on 05-Oct-16.
 */
public class HomeFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener, MoviesListener {

    private final String LOG_TAG = HomeFragment.class.getSimpleName();

    RecyclerView moviesHomeView;
    public HomeAdapter homeAdapter;

    private SwipeRefreshLayout swipeToRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        moviesHomeView = (RecyclerView) rootView.findViewById(R.id.moviesHomeRecyclerView);
        moviesHomeView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        swipeToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(R.color.colorPrimaryDark),getResources().getColor(R.color.colorAccent));
        swipeToRefresh.setOnRefreshListener(this);

        updateMovies();

        return rootView;
    }

    public void updateMovies() {
        MovieManager.getInstance().getMovies(this);
    }

    // TODO any other method ama y3'yr el preferences ttndh bdl ma y3ml onStart
    @Override
    public void onStart() {
        updateMovies();
        super.onStart();
    }

    @Override
    public void onRefresh() {
        updateMovies();
    }

    @Override
    public void onDownloadFinished(ArrayList<Movie> movies) {
        swipeToRefresh.setRefreshing(false);

        homeAdapter = new HomeAdapter(getActivity(), movies, new OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie item) {
                Intent goToMovieViewActivity = new Intent(getActivity(), MovieViewActivity.class);
                goToMovieViewActivity.putExtra(MovieContract.MovieTable.COLOUMN_ID, item.getId());
                goToMovieViewActivity.putExtra(MovieContract.MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE, item.getOriginal_title());
                goToMovieViewActivity.putExtra(MovieContract.MovieTable.COLOUMN_MOVIE_POSTER, item.getPoster_path());
                goToMovieViewActivity.putExtra(MovieContract.MovieTable.COLOUMN_MOVIE_OVERVIEW, item.getOverview());
                goToMovieViewActivity.putExtra(MovieContract.MovieTable.COLOUMN_MOVIE_AVERAGE_VOTING, item.getVote_average());
                goToMovieViewActivity.putExtra(MovieContract.MovieTable.COLOUMN_MOVIE_RELEASE_DATE, item.getRelease_date());
               // goToMovieViewActivity.putExtra(MovieContract.MovieTable.COLOUMN_MOVIE_FAVOURITE, item.isFavourite());
                goToMovieViewActivity.putExtra(MovieContract.MovieTable.COLOUMN_MOVIE_FAVOURITE, MovieDataSource.getInstance().isFav(item.getId()));
                startActivity(goToMovieViewActivity);
            }
        });
        moviesHomeView.setAdapter(homeAdapter);
    }

    @Override
    public void onFail(Exception e) {
        swipeToRefresh.setRefreshing(false);
        Utilities.noInternet();
    }
}
