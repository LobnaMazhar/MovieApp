package com.mal.lobna.movieapp.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mal.lobna.movieapp.Activity.HomeActivity;
import com.mal.lobna.movieapp.Activity.MovieViewActivity;
import com.mal.lobna.movieapp.Adapter.TrailerAdapter;
import com.mal.lobna.movieapp.Data.MovieContract;
import com.mal.lobna.movieapp.Managers.TrailerManager;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.Models.Trailer;
import com.mal.lobna.movieapp.Listeners.OnTrailerClickListener;
import com.mal.lobna.movieapp.R;
import com.mal.lobna.movieapp.Listeners.TrailerListener;
import com.mal.lobna.movieapp.Utilities.Utilities;

import java.util.ArrayList;

/**
 * Created by Lobna on 18-Nov-16.
 */

public class TrailersFragment extends android.support.v4.app.Fragment implements TrailerListener {

    private static RecyclerView movieTrailersRecyclerView;
    private TrailerAdapter trailerAdapter;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_trailers, container, false);

        movieTrailersRecyclerView = (RecyclerView) rootView.findViewById(R.id.movieTrailersRecyclerView);
        movieTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getTrailers();

        return rootView;
    }

    public void getTrailers(){
        Bundle arguments = null;
        if(getActivity().getClass().equals(MovieViewActivity.class)) {
            arguments = ((MovieViewActivity)getActivity()).getArguments();
        }else if(getActivity().getClass().equals(HomeActivity.class)){
            arguments = ((HomeActivity)getActivity()).getArguments();
        }

        if(arguments != null) {
            int movieID = ((Movie) arguments.getSerializable("item")).getId();
            TrailerManager.getInstance().getTrailers(movieID, this);
        }
    }

    @Override
    public void onResume() {
        getTrailers();
        super.onResume();
    }

    @Override
    public void onDownloadFinished(final ArrayList<Trailer> trailers) {
        TextView trailersTextView = (TextView) rootView.findViewById(R.id.trailersTextView);
        trailersTextView.setVisibility(View.VISIBLE);

        trailerAdapter = new TrailerAdapter(getActivity(), trailers, new OnTrailerClickListener() {
            @Override
            public void onTrailerClick(Trailer trailer) {
                watchYoutubeVideo(trailer.getKey());
            }
        });
        movieTrailersRecyclerView.setAdapter(trailerAdapter);
    }

    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void onFail(Exception e) {
        Utilities.noInternet(getActivity());
    }
}
