package com.mal.lobna.movieapp.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.Listeners.OnMovieClickListener;
import com.mal.lobna.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lobna on 05-Oct-16.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private final String LOG_TAG = HomeAdapter.class.getSimpleName();

    private Activity activity;
    private static ArrayList<Movie> movies;
    private OnMovieClickListener listener;

    public HomeAdapter(Activity activity, ArrayList<Movie> movies, OnMovieClickListener listener) {
        this.activity = activity;
        this.movies = movies;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = activity.getLayoutInflater().inflate(R.layout.movie_griditem, parent, false);

        return new HomeAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String baseURL = "http://image.tmdb.org/t/p/";
        String size = "w342/";
        String posterPath = movies.get(position).getPoster_path();

        Log.v(LOG_TAG, baseURL + size + posterPath);
        Picasso.with(activity).load(baseURL + size + posterPath).into(holder.moviePosterImageView);
        holder.moviePosterProgressBarGridItem.setVisibility(View.GONE);

        holder.bind(movies.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePosterImageView;
        ProgressBar moviePosterProgressBarGridItem;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.moviePosterImageViewGridItem);
            moviePosterProgressBarGridItem = (ProgressBar) itemView.findViewById(R.id.moviePosterProgressBarGridItem);
            if (moviePosterProgressBarGridItem.getVisibility() == View.GONE)
                moviePosterProgressBarGridItem.setVisibility(View.VISIBLE);
        }

        public void bind(final Movie item, final OnMovieClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMovieClick(item);
                }
            });
        }
    }

    public static void clearAdapter() {
        if (movies != null)
            movies.clear();
    }
}
