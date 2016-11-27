package com.mal.lobna.movieapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mal.lobna.movieapp.Activity.HomeActivity;
import com.mal.lobna.movieapp.Activity.MovieViewActivity;
import com.mal.lobna.movieapp.Data.MovieContract;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.R;

/**
 * Created by Lobna on 13-Nov-16.
 */

public class MovieViewFragment extends android.support.v4.app.Fragment {

    private Movie movie;
    private View rootView;
    private Bundle arguments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movie_view, container, false);

        return rootView;
    }

    private void setData(View view) {
        TextView movieOverviewTextView = (TextView) view.findViewById(R.id.movieOverviewTextView);
        movieOverviewTextView.setText(movie.getOverview());

        TextView movieAverageVoteTextView = (TextView) view.findViewById(R.id.movieAverageVoteTextView);
        movieAverageVoteTextView.setText("The average votes for this movie is " + movie.getVote_average());

        TextView movieReleaseDateTextView = (TextView) view.findViewById(R.id.movieReleaseDateTextView);
        movieReleaseDateTextView.setText("This movie was released on " + movie.getRelease_date());
    }

    @Override
    public void onResume() {
        if(getActivity().getClass().equals(MovieViewActivity.class)) {
            arguments = ((MovieViewActivity)getActivity()).getArguments();
        }else if(getActivity().getClass().equals(HomeActivity.class)){
            arguments = ((HomeActivity)getActivity()).getArguments();
        }

        if (arguments != null) {
            movie = (Movie) arguments.getSerializable("item");
            if (movie != null) {
                setData(rootView);
            }
        }
        super.onResume();
    }
}
