package com.mal.lobna.movieapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mal.lobna.movieapp.Data.MovieContract;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.R;

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
        int id = getActivity().getIntent().getExtras().getInt(MovieContract.MovieTable.COLOUMN_ID);
        movie.setId(id);

        String originalTitle = getActivity().getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE);
        movie.setOriginal_title(originalTitle);

        String posterPath = getActivity().getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_POSTER);
        movie.setPoster_path(posterPath);

        String overview = getActivity().getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_OVERVIEW);
        TextView movieOverviewTextView = (TextView) view.findViewById(R.id.movieOverviewTextView);
        movieOverviewTextView.setText(overview);
        movie.setOverview(overview);

        String voteAverage = getActivity().getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_AVERAGE_VOTING);
        TextView movieAverageVoteTextView = (TextView) view.findViewById(R.id.movieAverageVoteTextView);
        movieAverageVoteTextView.setText("The average votes for this movie is " + voteAverage);
        movie.setVote_average(voteAverage);

        String releaseDate = getActivity().getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_RELEASE_DATE);
        TextView movieReleaseDateTextView = (TextView) view.findViewById(R.id.movieReleaseDateTextView);
        movieReleaseDateTextView.setText("This movie was released on " + releaseDate);
        movie.setRelease_date(releaseDate);

        boolean favourite = getActivity().getIntent().getExtras().getBoolean(MovieContract.MovieTable.COLOUMN_MOVIE_FAVOURITE);
        movie.setFavourite(favourite);
    }
}
