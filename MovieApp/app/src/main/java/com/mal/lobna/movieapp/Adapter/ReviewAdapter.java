package com.mal.lobna.movieapp.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Listeners.OnTrailerClickListener;
import com.mal.lobna.movieapp.Models.Review;
import com.mal.lobna.movieapp.Models.Trailer;
import com.mal.lobna.movieapp.R;

import java.util.ArrayList;

/**
 * Created by Lobna on 25-Nov-16.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<Review> reviews;

    public ReviewAdapter(Activity activity, ArrayList<Review> reviews){
        this.activity = activity;
        this.reviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        if(activity != null) {
            rootView = activity.getLayoutInflater().inflate(R.layout.movie_reviewitem, parent, false);
        }
        else{
            rootView = parent.findViewById(R.id.movieReviewItemLayout);
           // rootView = View.inflate(MovieApplication.getMovieApp().getApplicationContext(), R.layout.movie_reviewitem, parent);
        }


        return new ReviewAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.reviewText.setText(reviews.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView reviewText;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewText = (TextView) itemView.findViewById(R.id.reviewText);
        }
    }
}
