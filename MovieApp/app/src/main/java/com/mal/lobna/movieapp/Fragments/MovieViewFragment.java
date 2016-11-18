package com.mal.lobna.movieapp.Fragments;

import android.app.Application;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mal.lobna.movieapp.Activity.MovieViewActivity;
import com.mal.lobna.movieapp.Adapter.HomeAdapter;
import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Data.MovieDataSource;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.OnItemClickListener;
import com.mal.lobna.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lobna on 13-Nov-16.
 */

public class MovieViewFragment extends android.support.v4.app.Fragment {

    Movie movie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_view, container, false);

        movie = new Movie();
        getData(rootView);

        return rootView;
    }

    private void getData(View view) {
        String originalTitle = getActivity().getIntent().getExtras().getString(HomeFragment.ORIGINAL_TITLE_KEY);
        movie.setOriginal_title(originalTitle);

        String baseURL = "http://image.tmdb.org/t/p/";
        String size = "original/";
        String posterPath = getActivity().getIntent().getExtras().getString(HomeFragment.MOVIE_POSTER_KEY);
        movie.setPoster_path(posterPath);

        String overview = getActivity().getIntent().getExtras().getString(HomeFragment.OVERVIEW_KEY);
        TextView movieOverviewTextView = (TextView) view.findViewById(R.id.movieOverviewTextView);
        movieOverviewTextView.setText(overview);
        movie.setOverview(overview);

        String voteAverage = getActivity().getIntent().getExtras().getString(HomeFragment.AVERAGE_VOTING_KEY);
        TextView movieAverageVoteTextView = (TextView) view.findViewById(R.id.movieAverageVoteTextView);
        movieAverageVoteTextView.setText("The average votes for this movie is " + voteAverage);
        movie.setVote_average(voteAverage);

        String releaseDate = getActivity().getIntent().getExtras().getString(HomeFragment.RELEASE_DATE_KEY);
        TextView movieReleaseDateTextView = (TextView) view.findViewById(R.id.movieReleaseDateTextView);
        movieReleaseDateTextView.setText("This movie was released on " + releaseDate);
        movie.setRelease_date(releaseDate);
    }
}
