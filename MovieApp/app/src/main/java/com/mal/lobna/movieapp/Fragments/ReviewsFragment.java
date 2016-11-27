package com.mal.lobna.movieapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mal.lobna.movieapp.Activity.HomeActivity;
import com.mal.lobna.movieapp.Activity.MovieViewActivity;
import com.mal.lobna.movieapp.Adapter.ReviewAdapter;
import com.mal.lobna.movieapp.Data.MovieContract;
import com.mal.lobna.movieapp.Listeners.ReviewListener;
import com.mal.lobna.movieapp.Managers.ReviewManager;
import com.mal.lobna.movieapp.Managers.TrailerManager;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.Models.Review;
import com.mal.lobna.movieapp.R;
import com.mal.lobna.movieapp.Utilities.Utilities;

import java.util.ArrayList;

/**
 * Created by Lobna on 25-Nov-16.
 */

public class ReviewsFragment extends Fragment implements ReviewListener {

    private static RecyclerView movieReviewsRecyclerView;
    private ReviewAdapter reviewAdapter;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null)
            rootView = inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        movieReviewsRecyclerView = (RecyclerView) rootView.findViewById(R.id.movieReviewsRecyclerView);
        movieReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getReviews();

        return rootView;
    }

    public void getReviews(){
        Bundle arguments = null;
        if(getActivity().getClass().equals(MovieViewActivity.class)) {
            arguments = ((MovieViewActivity)getActivity()).getArguments();
        }else if(getActivity().getClass().equals(HomeActivity.class)){
            arguments = ((HomeActivity)getActivity()).getArguments();
        }

        if(arguments != null) {
            int movieID = ((Movie) arguments.getSerializable("item")).getId();
            ReviewManager.getInstance().getReviews(movieID, this);
        }
    }

    @Override
    public void onResume() {
        getReviews();
        super.onResume();
    }

    @Override
    public void onDownloadFinished(ArrayList<Review> reviews) {
        reviewAdapter = new ReviewAdapter(getActivity(), reviews);
        movieReviewsRecyclerView.setAdapter(reviewAdapter);
    }

    @Override
    public void onFail(Exception e) {
        Utilities.noInternet();
    }
}
